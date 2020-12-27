-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 30, 2020 at 08:35 AM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fds_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bndf`
--

CREATE TABLE `bndf` (
  `mainID` int(3) NOT NULL,
  `QUANTITY` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `bndf`
--

INSERT INTO `bndf` (`mainID`, `QUANTITY`) VALUES
(1, '');

-- --------------------------------------------------------

--
-- Table structure for table `catf`
--

CREATE TABLE `catf` (
  `OTHER_FILES` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `catf`
--

INSERT INTO `catf` (`OTHER_FILES`) VALUES
('');

-- --------------------------------------------------------

--
-- Table structure for table `clip`
--

CREATE TABLE `clip` (
  `MAXIMUM_DENSITY` varchar(100) NOT NULL,
  `MAXIMUM_TEMPERATURE` varchar(100) NOT NULL,
  `MINIMUM_DENSITY` varchar(100) NOT NULL,
  `MINIMUM_TEMPERATURE` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clip`
--

INSERT INTO `clip` (`MAXIMUM_DENSITY`, `MAXIMUM_TEMPERATURE`, `MINIMUM_DENSITY`, `MINIMUM_TEMPERATURE`) VALUES
('', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `comb`
--

CREATE TABLE `comb` (
  `FIXED_MIX_TIME` varchar(100) NOT NULL,
  `EXTINCTION_MODEL` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `comb`
--

INSERT INTO `comb` (`FIXED_MIX_TIME`, `EXTINCTION_MODEL`) VALUES
('', '');

-- --------------------------------------------------------

--
-- Table structure for table `ctrl`
--

CREATE TABLE `ctrl` (
  `mainID` int(3) NOT NULL,
  `INPUT_ID` varchar(100) NOT NULL,
  `RAMP_ID` varchar(100) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `LATCH` varchar(100) NOT NULL,
  `FUNCTION_TYPE` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ctrl`
--

INSERT INTO `ctrl` (`mainID`, `INPUT_ID`, `RAMP_ID`, `ID`, `LATCH`, `FUNCTION_TYPE`) VALUES
(1, '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `devc`
--

CREATE TABLE `devc` (
  `mainID` int(3) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `PROP_ID` varchar(100) NOT NULL,
  `SPEC_ID` varchar(100) NOT NULL,
  `XYZ` varchar(100) NOT NULL,
  `QUANTITY` varchar(100) NOT NULL,
  `IOR` varchar(100) NOT NULL,
  `XB` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `devc`
--

INSERT INTO `devc` (`mainID`, `ID`, `PROP_ID`, `SPEC_ID`, `XYZ`, `QUANTITY`, `IOR`, `XB`) VALUES
(1, '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `dump`
--

CREATE TABLE `dump` (
  `MASS_FILE` varchar(100) NOT NULL,
  `SMOKE_3D` varchar(100) NOT NULL,
  `NFRAMES` varchar(100) NOT NULL,
  `DT_DEVC` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dump`
--

INSERT INTO `dump` (`MASS_FILE`, `SMOKE_3D`, `NFRAMES`, `DT_DEVC`) VALUES
('', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `head`
--

CREATE TABLE `head` (
  `CHID` varchar(100) NOT NULL,
  `TITLE` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `head`
--

INSERT INTO `head` (`CHID`, `TITLE`) VALUES
('', '');

-- --------------------------------------------------------

--
-- Table structure for table `hole`
--

CREATE TABLE `hole` (
  `MESH_ID` varchar(100) NOT NULL,
  `MULT_ID` varchar(100) NOT NULL,
  `DEVC_ID` varchar(100) NOT NULL,
  `CTRL_ID` varchar(100) NOT NULL,
  `XB` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hole`
--

INSERT INTO `hole` (`MESH_ID`, `MULT_ID`, `DEVC_ID`, `CTRL_ID`, `XB`) VALUES
('', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `hvac`
--

CREATE TABLE `hvac` (
  `ID` varchar(100) NOT NULL,
  `ROUGHNESS` varchar(100) NOT NULL,
  `DEVC_ID` varchar(100) NOT NULL,
  `LENGTH` varchar(100) NOT NULL,
  `FAN_ID` varchar(100) NOT NULL,
  `AREA` varchar(100) NOT NULL,
  `TYPE_ID` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hvac`
--

INSERT INTO `hvac` (`ID`, `ROUGHNESS`, `DEVC_ID`, `LENGTH`, `FAN_ID`, `AREA`, `TYPE_ID`) VALUES
('', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `init`
--

CREATE TABLE `init` (
  `mainID` int(3) NOT NULL,
  `idText` varchar(100) NOT NULL,
  `partIdText` varchar(100) NOT NULL,
  `specIdText` varchar(100) NOT NULL,
  `npartText` varchar(100) NOT NULL,
  `npartCellText` varchar(100) NOT NULL,
  `massTimeText` varchar(100) NOT NULL,
  `massVolText` varchar(100) NOT NULL,
  `massFracText` varchar(100) NOT NULL,
  `xbText` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `init`
--

INSERT INTO `init` (`mainID`, `idText`, `partIdText`, `specIdText`, `npartText`, `npartCellText`, `massTimeText`, `massVolText`, `massFracText`, `xbText`) VALUES
(1, '', '', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `isof`
--

CREATE TABLE `isof` (
  `QUANTITY` varchar(100) NOT NULL,
  `VALUE_1` varchar(100) NOT NULL,
  `VALUE_2` varchar(100) NOT NULL,
  `VALUE_3` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `isof`
--

INSERT INTO `isof` (`QUANTITY`, `VALUE_1`, `VALUE_2`, `VALUE_3`) VALUES
('', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `matl`
--

CREATE TABLE `matl` (
  `mainID` int(3) NOT NULL,
  `SPECIFIC_HEAT` varchar(100) NOT NULL,
  `HEAT_OF_REACTION` varchar(100) NOT NULL,
  `SPEC_ID` varchar(100) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `REFERENCE_TEMPERATURE` varchar(100) NOT NULL,
  `N_REACTIONS` varchar(100) NOT NULL,
  `DENSITY` varchar(100) NOT NULL,
  `CONDUCTIVITY` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `matl`
--

INSERT INTO `matl` (`mainID`, `SPECIFIC_HEAT`, `HEAT_OF_REACTION`, `SPEC_ID`, `ID`, `REFERENCE_TEMPERATURE`, `N_REACTIONS`, `DENSITY`, `CONDUCTIVITY`) VALUES
(1, '', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `mesh`
--

CREATE TABLE `mesh` (
  `mainID` varchar(100) NOT NULL,
  `ijkText` varchar(100) NOT NULL,
  `xbText` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mesh`
--

INSERT INTO `mesh` (`mainID`, `ijkText`, `xbText`) VALUES
('', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `misc`
--

CREATE TABLE `misc` (
  `NOISE` varchar(100) NOT NULL,
  `FREEZE_VELOCITY` varchar(100) NOT NULL,
  `HUMIDITY` varchar(100) NOT NULL,
  `Y_CO2_INFNTY` varchar(100) NOT NULL,
  `TMPA` varchar(100) NOT NULL,
  `GVEC` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `misc`
--

INSERT INTO `misc` (`NOISE`, `FREEZE_VELOCITY`, `HUMIDITY`, `Y_CO2_INFNTY`, `TMPA`, `GVEC`) VALUES
('', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `move`
--

CREATE TABLE `move` (
  `ID` varchar(100) NOT NULL,
  `X0` varchar(100) NOT NULL,
  `Y0` varchar(100) NOT NULL,
  `Z0` varchar(100) NOT NULL,
  `ROTATION_ANGLE` varchar(100) NOT NULL,
  `AXIS` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `move`
--

INSERT INTO `move` (`ID`, `X0`, `Y0`, `Z0`, `ROTATION_ANGLE`, `AXIS`) VALUES
('', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `mult`
--

CREATE TABLE `mult` (
  `mainID` int(3) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `I_UPPER` varchar(100) NOT NULL,
  `J_UPPER` varchar(100) NOT NULL,
  `K_UPPER` varchar(100) NOT NULL,
  `DX` varchar(100) NOT NULL,
  `DY` varchar(100) NOT NULL,
  `DZ` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mult`
--

INSERT INTO `mult` (`mainID`, `ID`, `I_UPPER`, `J_UPPER`, `K_UPPER`, `DX`, `DY`, `DZ`) VALUES
(1, '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `obst`
--

CREATE TABLE `obst` (
  `mainID` int(3) NOT NULL,
  `BULK_DENSITY` varchar(100) NOT NULL,
  `COLOR` varchar(100) NOT NULL,
  `SURF_ID` varchar(100) NOT NULL,
  `XB` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `obst`
--

INSERT INTO `obst` (`mainID`, `BULK_DENSITY`, `COLOR`, `SURF_ID`, `XB`) VALUES
(1, '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `part`
--

CREATE TABLE `part` (
  `mainID` int(3) NOT NULL,
  `SURF_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `SPEC_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `PROP_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `QUANTITIES` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `STATIC` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `MASSLESS` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `SAMPLING_FACTOR` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `DIAMETER` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `part`
--

INSERT INTO `part` (`mainID`, `SURF_ID`, `SPEC_ID`, `PROP_ID`, `QUANTITIES`, `STATIC`, `MASSLESS`, `SAMPLING_FACTOR`, `DIAMETER`, `ID`) VALUES
(1, '', '', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `pres`
--

CREATE TABLE `pres` (
  `FISHPAK_BC` varchar(100) NOT NULL,
  `SOLVER` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pres`
--

INSERT INTO `pres` (`FISHPAK_BC`, `SOLVER`) VALUES
('', '');

-- --------------------------------------------------------

--
-- Table structure for table `prof`
--

CREATE TABLE `prof` (
  `ID` varchar(100) NOT NULL,
  `XYZ` varchar(100) NOT NULL,
  `QUANTITY` varchar(100) NOT NULL,
  `IOR` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prof`
--

INSERT INTO `prof` (`ID`, `XYZ`, `QUANTITY`, `IOR`) VALUES
('', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `prop`
--

CREATE TABLE `prop` (
  `mainID` int(3) NOT NULL,
  `ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `PART_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `QUANTITY` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `SMOKEVIEW_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `OFFSET` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `PDPA_INTEGRATE` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `PDPA_NORMALIZE` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `OPERATING_PRESSURE` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `PARTICLES_PER_SECOND` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `PARTICLE_VELOCITY` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `prop`
--

INSERT INTO `prop` (`mainID`, `ID`, `PART_ID`, `QUANTITY`, `SMOKEVIEW_ID`, `OFFSET`, `PDPA_INTEGRATE`, `PDPA_NORMALIZE`, `OPERATING_PRESSURE`, `PARTICLES_PER_SECOND`, `PARTICLE_VELOCITY`) VALUES
(1, '', '', '', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `radf`
--

CREATE TABLE `radf` (
  `I_STEP` varchar(100) NOT NULL,
  `J_STEP` varchar(100) NOT NULL,
  `K_STEP` varchar(100) NOT NULL,
  `XB` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `radf`
--

INSERT INTO `radf` (`I_STEP`, `J_STEP`, `K_STEP`, `XB`) VALUES
('', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `radi`
--

CREATE TABLE `radi` (
  `RADIATION` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `radi`
--

INSERT INTO `radi` (`RADIATION`) VALUES
('');

-- --------------------------------------------------------

--
-- Table structure for table `ramp`
--

CREATE TABLE `ramp` (
  `mainID` int(3) NOT NULL,
  `FRACTION` varchar(100) NOT NULL,
  `TIME` varchar(100) NOT NULL,
  `ID` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ramp`
--

INSERT INTO `ramp` (`mainID`, `FRACTION`, `TIME`, `ID`) VALUES
(1, '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `reac`
--

CREATE TABLE `reac` (
  `mainID` int(3) NOT NULL,
  `AUTO_IGNITION_TEMPERATURE` varchar(100) NOT NULL,
  `SOOT_YIELD` varchar(100) NOT NULL,
  `FUEL` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reac`
--

INSERT INTO `reac` (`mainID`, `AUTO_IGNITION_TEMPERATURE`, `SOOT_YIELD`, `FUEL`) VALUES
(1, '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `slcf`
--

CREATE TABLE `slcf` (
  `mainID` int(3) NOT NULL,
  `QUANTITY` varchar(100) NOT NULL,
  `SPEC_ID` varchar(100) NOT NULL,
  `PBY` varchar(100) NOT NULL,
  `PBZ` varchar(100) NOT NULL,
  `PBX` varchar(100) NOT NULL,
  `VECTOR` varchar(100) NOT NULL,
  `CELL_CENTERED` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `slcf`
--

INSERT INTO `slcf` (`mainID`, `QUANTITY`, `SPEC_ID`, `PBY`, `PBZ`, `PBX`, `VECTOR`, `CELL_CENTERED`) VALUES
(1, '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `spec`
--

CREATE TABLE `spec` (
  `mainID` int(3) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `BACKGROUND` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `spec`
--

INSERT INTO `spec` (`mainID`, `ID`, `BACKGROUND`) VALUES
(1, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `surf`
--

CREATE TABLE `surf` (
  `mainID` int(3) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `PART_ID` varchar(100) NOT NULL,
  `MATL_ID` varchar(100) NOT NULL,
  `VEL` varchar(100) NOT NULL,
  `TMP_FRONT` varchar(100) NOT NULL,
  `BACKING` varchar(100) NOT NULL,
  `DEFAULT_SURF` varchar(100) NOT NULL,
  `GEOMETRY` varchar(100) NOT NULL,
  `COLOR` varchar(100) NOT NULL,
  `HRRPUA` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `surf`
--

INSERT INTO `surf` (`mainID`, `ID`, `PART_ID`, `MATL_ID`, `VEL`, `TMP_FRONT`, `BACKING`, `DEFAULT_SURF`, `GEOMETRY`, `COLOR`, `HRRPUA`) VALUES
(1, '', '', '', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tabl`
--

CREATE TABLE `tabl` (
  `mainID` int(3) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `TABLE_DATA` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tabl`
--

INSERT INTO `tabl` (`mainID`, `ID`, `TABLE_DATA`) VALUES
(1, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `time`
--

CREATE TABLE `time` (
  `EndTime` varchar(100) NOT NULL,
  `StartTime` varchar(100) NOT NULL,
  `DT` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `time`
--

INSERT INTO `time` (`EndTime`, `StartTime`, `DT`) VALUES
('', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `trnx`
--

CREATE TABLE `trnx` (
  `mainID` int(3) NOT NULL,
  `ID` varchar(100) NOT NULL,
  `MESH_NUMBER` varchar(100) NOT NULL,
  `CC` varchar(100) NOT NULL,
  `PC` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `trnx`
--

INSERT INTO `trnx` (`mainID`, `ID`, `MESH_NUMBER`, `CC`, `PC`) VALUES
(1, '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `vent`
--

CREATE TABLE `vent` (
  `mainID` int(3) NOT NULL,
  `XB` varchar(100) NOT NULL,
  `SURF_ID` varchar(100) NOT NULL,
  `MB` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vent`
--

INSERT INTO `vent` (`mainID`, `XB`, `SURF_ID`, `MB`) VALUES
(1, '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `wind`
--

CREATE TABLE `wind` (
  `Z_0` varchar(100) NOT NULL,
  `DIRECTION` varchar(100) NOT NULL,
  `L` varchar(100) NOT NULL,
  `SPEED` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `wind`
--

INSERT INTO `wind` (`Z_0`, `DIRECTION`, `L`, `SPEED`) VALUES
('', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `zone`
--

CREATE TABLE `zone` (
  `mainID` int(3) NOT NULL,
  `XYZ` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `zone`
--

INSERT INTO `zone` (`mainID`, `XYZ`) VALUES
(1, '');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
