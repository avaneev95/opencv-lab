# OpenCV Labs
This repo contains source code for the university OpenCV labs.  

### Lab 1
Basic image processing operations.   
Application features:
* Rotate image by an arbitrary angle;
* Convert image to grayscale;
* Flip image vertically;
* Flip image horizontally;
* Save result as PNG or JPEG file;

### Lab 2
Basic geometric shapes recognition. This application uses Canny algorithm 
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