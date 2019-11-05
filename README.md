### How to build and run Greeting application
Please make sure that your tools are installed and ready to use.

1.Build runnable jar: ```mvn clean install```

2.Build the docker image (in this case name greeting): ```docker build -t greeting .```

3.Run the docker image (greeting) and port forward your localhost 5000 to the image's 8080: 
```docker run -p 5000:8080 greeting```
