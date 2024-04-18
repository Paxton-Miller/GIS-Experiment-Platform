# GIS-Experiment-Platform
## Table of contents

- [Introduction](#introduction)
- [Main files](#main-files)
- [Getting started](#getting-started)
- - [Data Source](#data-source)
  - [Backend](#backend)
  - [Frontend](#frontend)
  - [Android](#Android)
- [Contributing](#contributing)
- [Contact us](#contact-us)

## Introduction

GIS-Experiment-Platform (**GeoTutor**——GIS专业虚拟网络实验平台) is a platform designed for GIS study in universities.

Development Mode: B/S Mode.

## Main Files

```
Code/
├── Geotutor_project	 (frontend project)
├── Geotutor_api_server  (backend project)
├── Geotutor_android_app (mobile terminal)

Data/
├── Geoserver DataSoure
├── GIS Experiment DataSoure
├── MySQL DataSoure
├── PostGIS DataSoure
```

## Getting started

### Data Source

##### You may want to install the following items and the attached environment properly:

- MySQL v5.7.43
- PostgreSQL v9.6.24
- PostGIS Bundle 3 for PostgreSQL x64 9.6
- Navicat Premium 16
- jdk 1.8
- Geoserver 2.20.0

##### Prep MySQL Database:

- You can create a new database named "**njtech**" and choose a utf8mb4 charset. 
- Execute the sql file (Data/MySQL DataSource) and you can view the data in Navicat.

##### Prep Potgresql Database:

- Open the pgAdmin and create a database named "China".
- Create an extension named "postgis".
- Open PostGIS Bundle 3 for PostgreSQL x64 9.6 Shapefile and DBF Loader Exporter
- Fill out the PostGIS Connection and add shp files (all files in Data/PostGIS DataSource).
- If import failed, try GBK-2312 in options.

##### Prep the Geoserver DataSource:

- Open Data/Geoserver DataSource/File Link(Do Open it so you can obtain the file).txt
- Copy Data/Geoserver DataSource/geoserver-2.20.0-bin.zip (all data and configuration files are saved here), paste all contents of the folder into the path of the device to be installed, and save it here to C: \ Users \ 23866 \ Desktop \ geoserver-2.20.0-bin; Add system environment variables as follows:
- - GEOSERVER-HOME (C: \ Users \ 23866 \ Desktop \ geoserver-2.20.0 bin), GEOSERVER-DATA-DIR (% GEOSERVER-HOME% \ data_dir);
- Copy Data/Geoserver DataDource, paste all contents except geoserver-2.20.0-bin.zip in the folder to the path where the device wants to save, and then save it to C: \ Users \ 23866 \ Desktop \ data;
- Double click C: \ Users \ 23866 \ Desktop \ geoserver-2.20.0 bin \ startup.bat to start geoserver;

- Input <http://localhost:8060/geoserver/web/> in your browser and login.
- Set the Directory of shapefiles as file://C:\Users\23866\Desktop\data\webgis_geoserver；(you can fine webgis_geoserver in Data/Geoserver DataSource)
- For workspace named "njtech_info", set the Directory of shapefiles as file:// C:\Users\23866\Desktop\data\ex8；
- Open the OpenLayers preview，the settings若正常显示则说明配置成功。

You can find the frontend project in Code/frontend/webgis_project, 

### Backend

Install the node-v19.1.0 and the related environment.

switch to the path root of the Code/GeoTutor_api_server and run the following in your terminal.

```
node app.js
```

### Frontend

Install the vue2.x, webpack and the related environment.

switch to the path root of the Code/GeoTutor_project and run the following in your terminal.

```
npm i
npm run serve
```

### Android

Based on Android Studio 2021.1.1, you can start the Code/GeoTutor_android_app in Android Studio.

You can also run the apk file(Code/Geotutor.apk) directly and test it.

## Contributing

The project is developed by Lingkai Shi and Di Qiang.

## Contact us

You can send an email to gislkshi@outlook.com if you hava any problems about the project.

[⬆ back to top](#table-of-contents)