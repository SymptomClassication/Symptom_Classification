MAINTAINER: Hala Al Kaisi

# Classify test symptoms

Used to do the classification of the symptoms given in the symptoms_test.pdf, based on the classifying chapters/subchapters in the database (populated using populate_database).

# Input and Output

The input is currently the pdf document called "Symptome_Test.pdf", created from the word document of the same name. Currently, scanning input is done using PyPDF2.
The output is in a form of a json file, with the symptoms, their chapters, subchapters(if nay), and subtitles(if any).

# How to run

What is required is Python > 3.10.0. Any required packages are installed automatically from the requirements.txt file when running the Makefile.

### run the following to execute


    make classify_tests
    