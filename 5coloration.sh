#!/bin/bash
# This script will take an animated GIF and delete every other frame
# Accepts two parameters: input file and output file
# Usage: ./<scriptfilename> input.gif output.gif

rm graphe.dot
rm graphe.png
javac *.java
java Main $1
dot -Tpng graphe.dot -o graphe.png
eog graphe.png
