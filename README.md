# OpenCV Labs
This repo contains source code for the university OpenCV labs.  

### Lab 1
Basic image processing operation.   
An application can:
* rotate image at an random angle;
* convert image to gray scale;
* flip vertically;
* flip horizontally;
* save result as PNG or JPEG file;

### Lab 2
Basic geometric shape recognition. This application uses Canny algorithm 
to detect edges on the given image and contour geometry analysis 
to classify detected contours.

### Tools used
* OpenCV 3.4.3
* Java 8

### Install OpenCV to local Maven repo     
```
$ mvn install:install-file \
      -Dfile=/path/to/jar/opencv-343.jar \
      -DgroupId=org.opencv \
      -DartifactId=opencv \
      -Dversion=3.4.3 \
      -Dpackaging=jar
```