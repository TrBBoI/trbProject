# This make file compiles and create an executable file.

# Makefile
# Author: Tan Zhe Xing (TrBBoI)
# Version: 0.8.2015.02.14

# The MIT License (MIT)

# Copyright (c) 2015 Tan Zhe Xing

# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:

# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

C=gcc
CC=g++
# TODO add your own addition flags
CFLAGS=-Wall -ansi -std=c++11
LDFLAGS=

# TODO add all external library e.g. -lSDL2 adds SDL2.a
EXT_LIB=

# TODO add all library path
LIB_PATH:=lib
# LIB:=$(foreach SRC_DIR, $(LIB_DIR), $(wildcard $(SRC_DIR)/*))

# TODO add all source and include directory path
INC_DIR:=src 
SRC_DIR:=src 
OBJ_DIR:=$(SRC_DIR:src%=obj%)

INC:=$(foreach inc, $(INC_DIR), -I$(inc))

# TODO change executable name
EXE:=application





# DO NOT EDIT

SRC:=$(foreach SRC_DIR, $(SRC_DIR), $(wildcard $(SRC_DIR)/*.cpp))
#OBJ:=$(SRC:src%.cpp=obj%.o)

CSRC:=$(foreach SRC_DIR, $(SRC_DIR), $(wildcard $(SRC_DIR)/*.c))
#COBJ:=$(CSRC:src%.c=obj%.o)

GCC_SRC:=$(filter-out %Win.cpp, $(SRC))
GCC_OBJ:=$(GCC_SRC:src%.cpp=obj%.o)

GCC_CSRC:=$(filter-out %Win.c, $(CSRC))
GCC_COBJ:=$(GCC_CSRC:src%.c=obj%.o)

GCC_EXE=$(EXE).out

all : $(OBJ_DIR) $(GCC_EXE)
	@echo "-= Compile and Link Complete =-"

gcc : $(OBJ_DIR) $(GCC_EXE)
	@echo "-= GCC Compile and Link Complet =-"

$(GCC_EXE) : $(GCC_OBJ) $(GCC_COBJ)
	$(CC) $(LDFLAGS) $(GCC_OBJ) $(GCC_COBJ) -L$(LIB_PATH) $(EXT_LIB) -o $(GCC_EXE)

$(GCC_OBJ) : $(GCC_SRC)
	$(CC) $(CFLAGS) $(INC) -c $(@:obj%.o=src%.cpp) -o $@

$(GCC_COBJ) : $(GCC_CSRC)
	$(C) $(CFLAGS) $(INC) -c $(@:obj%.o=src%.c) -o $@

$(OBJ_DIR) :
	mkdir -p $(OBJ_DIR)

clean: 
	rm -f -r -d $(GCC_OBJ) $(GCC_COBJ) $(GCC_EXE)

