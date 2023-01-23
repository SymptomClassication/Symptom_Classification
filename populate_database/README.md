MAINTAINER: Hala Al Kaisi

# Populate Database 

Used to do the initial population of the database with the initial chapters info from the provided word/odf document.

# Input and Output

The input is currently the pdf document called "Chapters.pdf", created from the word document provided "Chapter explanation & -structure". Currently, scanning input is done using PyPDF2.
The output is in a form of json files. The index in each refer to the same chapter (used for foreign key in the database). The output json files are then found in the folder "output_json_files".

# How to run

What is required is Python > 3.10.0. Any required packages are installed automatically from the requirements.txt file when running the Makefile.

### run the following to excute

    
    make populate
    