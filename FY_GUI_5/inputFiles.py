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