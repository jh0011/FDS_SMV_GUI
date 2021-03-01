import os, itertools, shutil, re, string, sys
import numpy as np

global_file_value = 0

def dict_product(dicts):
	"""
	dict_product(dicts)

	from a dict of parameters creates a generator which outputs a list of dicts 
	effectively a cartesian product over the parameter space.

	eg: from:  	{'a': [0,1], 'b': [2,3], 'c': [4]}

	outputs:   [{'a': 0, 'b': 2, 'c': 4},
				{'a': 0, 'b': 3, 'c': 4},
				{'a': 1, 'b': 2, 'c': 4},
				{'a': 1, 'b': 3, 'c': 4}]
	"""
	# from http://stackoverflow.com/questions/5228158/cartesian-product-of-a-dictionary-of-lists
	return (dict(itertools.izip(dicts, x)) for x in itertools.product(*dicts.itervalues()))

def dict_builder(params, test_name = ''): 
	"""
	dict_builder(params)

	uses the dict_product function and adds a 
	title key value pair for use in the input files
	
	eg: from:  	{'a': [0,1], 'b': [2,3], 'c': [4]}

	outputs:    [{'TITLE': 'STEP_BOX0-4-2', 'a': '0', 'b': '2', 'c': '4'},
				 {'TITLE': 'STEP_BOX0-4-3', 'a': '0', 'b': '3', 'c': '4'},
				 {'TITLE': 'STEP_BOX1-4-2', 'a': '1', 'b': '2', 'c': '4'},
				 {'TITLE': 'STEP_BOX1-4-3', 'a': '1', 'b': '3', 'c': '4'}]
	"""

	for value_set in dict_product(params):
		title = "-".join(map(str, value_set.values())).replace('.', '_')
		vals = [dict([a, str(x)] for a, x in value_set.iteritems())]
		vals = vals[0]
		vals['TITLE'] = test_name + title
		yield vals

def input_directory_builder(folder_name, base_path):
	"""
	input_directory_builder(folder_name, base_path)

	taking a base_path and a particular folder (folder_name) and creating both 
	folder_name inside of base_path if base_path does not exist and if base_path
	does exist, just creates folder_name inside of it.
	"""
	calling_dir = os.getcwd()
	
	if not os.path.exists(os.path.join(calling_dir, base_path)):
		os.mkdir(base_path)

	try:
		os.chdir(os.path.join(calling_dir, base_path))
		os.mkdir(folder_name)
	except:
		raise
	finally:
		os.chdir(calling_dir)

def build_input_files(filename, base_path = 'input_files', out = sys.stdout):
    """
    build_input_files(filename, base_path = 'input_files')
    
    takes a 'well-formated' input fileand outputs a 
    directory structure with the properly formated input files
    created in them. 
    """
    calling_dir = os.getcwd()
    
    # I'm doing this because I need it later
    file_path, file_name = os.path.split(filename)
    
    with open('input_file', 'r') as f:
        txt = f.read()
    #with open(filename, 'r') as f:
    #    txt = f.read()
    
    param_dict, txt, IOoutput = FDSa_parser(txt, file_name, out) #Get a dict of the values
    formatted_trials, logfile, IOoutput = eval_parsed_FDS(param_dict, out)
    for i, value_set in enumerate(formatted_trials):
        tmp_txt = txt
        # make a directory
        case_name = 'case_'+int2base(i, 26)
        # FDS uses uppercase reseved keywords, and so will we
        value_set['TITLE'] = case_name
        input_directory_builder(case_name, base_path)
        # populate the input file
        tmp_txt = tmp_txt.format(**value_set)
        # create the file name
        fname = os.path.join(calling_dir, base_path, 
                    case_name, case_name + '.fds')
        # write the input file to the directory
        with open(fname, 'w') as f:
            f.write(str(tmp_txt))
        
    log_path_name = os.path.join(calling_dir, base_path, file_name[:-4] + '.log')
    
    # write the augmented fds log file

    with open(log_path_name, 'a') as f:
        f.write(logfile)
        
    return IOoutput
    
def input_file_paths(base_path):
	"""
	input_file_paths(base_path) 

	returns the paths of the input files present by recusivly walking over them
	so they can be iterated over in the main body of the program via multiprocessing
	"""
	paths = []
	for dirpath, dirnames, filenames in os.walk(base_path):
		for onefile in filenames:
			# the following if statement is due to OS X .DsStore bullshit...
			if not (onefile.startswith('.DS') or onefile.endswith('.log')):
				#paths.append(dirpath+"/"+onefile)      
				paths.append(os.path.join(os.getcwd(), dirpath, onefile))
	return paths
	
def int2base(x, base=26):
    """
    int2base(x, base) 

    takes an integer and returns the base 26 representation (defualt) in letters
    like one would see in excel column labeling (0 -> a, 63 -> cl)
    
    based on https://stackoverflow.com/questions/2267362
    """
    digs = string.lowercase
    
    assert type(x) is int, "x is not an integer: %r" % x
    assert type(base) is int, "base is not an integer: %r" % base
    
    if x < 0: sign = -1
    elif x==0: return 'a'
    else: sign = 1
    x *= sign
    digits = []
    y = x
    while y:
        digits.append(digs[x % base])
        y = x / base
        x = (x / base) - 1
    if sign < 0:
        digits.append('-')
        digits.reverse()
    # 
    return ''.join(digits)[::-1]

def FDSa_parser(txt, filename, IOoutput=sys.stdout):
    """
    FDSa_parser(txt, filename, IOoutput) 

    takes in an augmented FDS file and determines how many 
    parametric will be created from that it also parses the augmented syntax to 
    build the dictionary used in generating the specific case FDS files
    """
    ## I'm doing this because I need it later
    #file_path, file_name = os.path.split(filename)
    # open the augmented fds input file
    #with open(os.path.join(file_path, file_name), 'r') as f:
    #    read_data = f.read()
        
    regex_find = re.findall('\{*[0-9a-zA-Z_:,.\s]*\}', txt)

    params = []
    params_raw = []

    params_string = [] #params_raw for LIST
    params_string_split = [] #params for LIST
    params_replace = []
    
    for param in regex_find:
        if 'LIST' not in param:
            params_raw.append(param.strip('{}'))
            params.append(param.strip('{}').split('SWEEP'))
        else:
            params_replace.append(param)
            params_string.append(param.strip('{}'))
            params_string_split.append(param.strip('{}').split('LIST'))


    # Updates the txt for SWEEP option
    txt1, param_dict = updating_txt(txt, params, params_raw)

    # Updates the txt for the LIST section
    txt2 = txt
    for x in range(len(params_replace)):
        temp_txt = txt2
        temp_list_1 = params_replace[x].split("LIST")
        to_replace = temp_list_1[0].replace(" ", "") + "}"
        txt2 = temp_txt.replace(params_replace[x], to_replace)
    
    
    IOoutput.write('-'*10 + 'ParFDS input file interpreter' + '-'*10 + '\n')
    IOoutput.write('the following are the keys and values'+ '\n')
    IOoutput.write('seen in ' + filename + '\n')



    # For the list of values in string
    param_strings_dict = {}
    param_format_list = []

    # Creates a list of Key and Value, alternatively
    # param_format_list is the name of the list 
    for param in params_string:
        temp_list = param.split('LIST')
        for word in temp_list:
            param_format_list.append(word.replace(" ", ""))

    # Converts the above list into a dictionary
    # list[even number] is the Key
    # list[odd number] is the Value
    # param_strings_dict is the name of the dictionary
    value = ""
    for x in range(len(param_format_list)):
        if x % 2 == 0:
            key = param_format_list[x]
        else:
            value = param_format_list[x]
        param_strings_dict[key] = value

    
    
    # Append both dictionaries to the list
    # list[0] = Dictionary for numeric values
    # list[1] = Dictionary for the string values
    newlist = []
    newlist.append(param_dict)
    newlist.append(param_strings_dict)

    # If both SWEEP and LIST are present, throw an error and exit from execution.
    if (len(param_dict) > 0 and len(param_strings_dict) > 0):
        print('ENTERED HERE')
        sys.exit('Either SWEEP (for numeric values) or LIST (for string values) should be used for an input file. Not both simultaneously.')     
    
    # If SWEEP option is chosen, return txt1
    if (len(param_dict) > 0):
        return newlist, txt1, IOoutput
    # If LIST option is chosen, return txt2
    else:
        return newlist, txt2, IOoutput



def updating_txt(txt, params, params_raw):
    # for the list of numeric values
    params = [item.strip() for sublist in params for item in sublist]
    
    # if length of params is non-even that means I can assume a title parameter
    # double check with the occurance of FDSa 'reserved' keywords 'title' or 'name'
    if (len(params) % 2 != 0) and (params[0].lower() == ('title')):
        # based on the following idiom 
        # https://stackoverflow.com/questions/3303213
        param_dict = dict(zip(params[1::2], params[2::2]))
        param_list = [params[0]]
        param_list.extend(params[1::2])
        param_name_dict = dict(zip(param_list, params_raw))
    else:
        param_dict = dict(zip(params[::2], params[1::2]))
        param_list = params[::2]
        param_name_dict = dict(zip(param_list, params_raw)) #{'WALL_TEMP': 'WALL_TEMP SWEEP 200, 400, 4'}
    # dealing with the `:` and `.` issue in the FDS file due to 
    # key value restrictions in python 
    for key, value in param_name_dict.iteritems():
        txt = string.replace(txt, value, key)
    
    return txt, param_dict

def eval_parsed_FDS(input_list, IOoutput = sys.stdout):
  
    """
    eval_parsed_FDS(param_dict, IOoutput = sys.stdout) 

    takes the dictionary that is returned by FDSa_parser and actually evaluates 
    it to create python readable arrays that can be broken up for the parametric studies.
    """
    param_dict = input_list[0]
    param_dict_2 = input_list[1]


    if (len(param_dict) > 0 and len(param_dict_2) > 0):
        sys.exit('Either SWEEP (for numeric values) or LIST (for string values) should be used for an input file. Not both simultaneously.')

    permutations = 1
    formatted_trials = []
    logfile = ''

    if (len(param_dict_2) > 0):
        # Call another function 
        # Return permutations and param dict
        formatted_trials_2,logfile = parse_Values(param_dict_2, permutations)
        IOoutput.write(logfile)
        #return formatted_trials, logfile, IOoutput

        for x in range(len(formatted_trials_2)):
            formatted_trials.append(formatted_trials_2[x])


    elif (len(param_dict) >= 0):
        for key, value in param_dict.iteritems():
            
            value_str = 'np.linspace(' + value.replace("'", "") +')'
            
            param_dict[key] = eval(value_str, {"__builtins__":None}, 
                                {"np": np,"np.linspace":np.linspace,"np.logspace":np.logspace})
            value_split = value.split(',')
            
            assert float(value_split[2]) >= 1, "the number of steps is not an integer: %r" % float(value_split[2].strip())
            
            permutations *= int(value_split[2])
            
            IOoutput.write(key + ' varied between ' + str(value_split[0]) +\
                ' and ' + str(value_split[1]) + ' in ' + str(value_split[2]) + ' step(s)' + '\n')
                
        
        IOoutput.write('-'*10 + ' '*10 + '-'*10 + ' '*10 + '-'*10 + '\n') 
        IOoutput.write('for a total of ' + str(permutations) + ' trials' + '\n')
        
        trials = dict_product(param_dict)

        logfile = 'There are a total of ' + str(permutations) + ' trials \n'
        newline = '\n' # for the newlines
        
        
        base = 26
        for i, v in enumerate(trials):
            case_temp = 'case ' + int2base(i, base) + ': '
            logfile += case_temp
            IOoutput.write(case_temp,)
            for key, val in v.iteritems():
                kv_temp = key + ' ' + str(round(val, 2)) + ' '
                logfile += kv_temp + ' '
                IOoutput.write(kv_temp,)
            IOoutput.write(newline)
            logfile += newline
            formatted_trials.append({key : value for key, value in v.items() })

    """
    >>> important_dict = {'x':1, 'y':2, 'z':3}
    >>> name_replacer = {'x':'a', 'y':'b', 'z':'c'}
    >>> {name_replacer[key] : value for key, value in important_dict.items() }
    {'a': 1, 'b': 2, 'c': 3}
    """    

    

    # IOoutput.getvalue().strip()
    return formatted_trials, logfile, IOoutput
    

def parse_Values(param_dict, permutations):
    formatted_list = []
    len_value_list = 0
    base = 26
    for key, value in param_dict.iteritems():
        value = value.replace(" ", "")
        value_list = value.split(',')
        len_value_list = len(value_list)
        param_dict[key] = value_list
        permutations *= len_value_list # find the number of permutations
    
    
    logfile = ""
    # Each list contains the possible values the input variable can take
    # param_list is a list of those lists
    param_list = [] 
    for key,value in param_dict.iteritems():
        param_list.append(param_dict[key])
    
    # A tuple of all possible combinations of the input (1 from each input variable) is created
    # temp_list is the list of those tuples
    temp_list = param_list[0]
    for x in range(len(param_list) - 1):
        temp_list = itertools.product(temp_list, param_list[x + 1])
    
    # Each list within temp_list is converted into a proper list (in terms of formatting)
    # list_of_lists is a list of those proper lists
    list_of_lists = []
    for x in temp_list:
        temp_x = str(x).replace("(", "").replace(")", "").replace(" ", "").replace("'", "")
        list_of_lists.append(temp_x.split(",")) 
    
    # Each input variable is assigned to a value from the list.
    # 1 full iteration is stored as a dictionary
    # formatted_list is a list of all those dictionaries
    count = 0
    for x in list_of_lists:
        temp_dict = {}
        for i in range(len(x)):
            temp_dict[param_dict.keys()[i]] = x[i]
        formatted_list.append(temp_dict)
        
        case_temp = 'case ' + int2base(count, base) + ': '
        logfile += case_temp
        case_values = str(temp_dict).replace("{", "").replace("}", "").replace("\"", "")
        logfile += case_values + '\n'

        count += 1
    return formatted_list, logfile # list of dictionaries
