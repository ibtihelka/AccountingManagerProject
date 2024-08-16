FROM selenium/standalone-chrome:latest

# Set up the working directory and copy application files
WORKDIR /app

# Copy the test application files to the image
COPY . /app

RUN sudo apt update \
&& sudo apt-get upgrade -y \
&& sudo apt-get install openjdk-17-jdk openjdk-17-doc -y \
&& sudo apt install maven -y

ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64/
RUN export JAVA_HOME && echo $JAVA_HOME

# Set the entry point or command for the container
CMD ["mvn", "clean", "test"]