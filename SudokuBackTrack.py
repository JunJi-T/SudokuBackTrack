#@author JunJi

'''

Sudoku Backtracking algorithm

'''

from collections import deque
import math

#Init sudoku grid
Grid = [['-' for h in range(9)] for v in range(9)]

#Create queue
queue = deque()

#Setup
def Setup(fileName):

	sudokuFile = open(fileName, "r")

	#populate grid from the txt file
	for h in range(9):
		i = 0
		sudokuText = sudokuFile.readline()
		for v in range(9):	
 			Grid[h][v] = sudokuText[i]
 			i += 1

	#populate queue
	for x in range(9):
 		for y in range(9):
 			if(Grid[x][y] == '-'):
 				queue.append(str(x) + str(y))
	return None


#Return a fancy diagram of the sudoku.
def GridToString():

	state = "+-----------------------+\n"

	for h in range(9):
		state += "|"
		for v in range(9):
			state += " " + Grid[h][v]
			if(v == 2 or v == 5):
				state += " |"
			if(v == 8):
				state += " |\n"
		if(h == 2 or h == 5):
			state += "|-------+-------+-------|\n"
	state += "+-----------------------+\n"

	return state


#check for any conflict in the row, column or box of this grid location(h,v)
def chkConflict(h,v):

	if(Grid[h][v] != '-'):
		#check horizontal(row)
		for i in range(9):
			if(i != v):
				if(Grid[h][i] == Grid[h][v]):
					return True


		#check vertical(column)
		for i in range(9):
			if(i != h):
				if(Grid[i][v] == Grid[h][v]):
					return True

		#check 3x3 box
		boxH = math.floor(h/3)*3
		boxV = math.floor(v/3)*3

		for x in range(boxH, boxH+3):
			for y in range(boxV, boxV+3):
				if( x != h and y != v):
					if(Grid[x][y] == Grid[h][v]):
						return True
	return False


#check if board is full(solved)
def isGridFull():

	for h in range(9):
		for v in range(9):
			if(Grid[h][v] == '-'):
				return False
	return True


#BackTracking algorithm
def backTrack():

	SUCCESS = True
	FAILED = False

	if(isGridFull()):
		return SUCCESS
	else:
		nextEmptyGrid = queue.popleft()
		
		GridH = nextEmptyGrid[0]
		GridV = nextEmptyGrid[1]

		#for all values in the var domain

		for i in range(9):
			value = i+1
			Grid[int(GridH)][int(GridV)] = str(value)
			if(chkConflict(int(GridH), int(GridV))):
				Grid[int(GridH)][int(GridV)] = '-'
			else:
				if(backTrack() == SUCCESS):
					return SUCCESS
				else:
					Grid[int(GridH)][int(GridV)] = '-'
		queue.appendleft(nextEmptyGrid)
		return FAILED


#main 
if __name__ == "__main__":
	Setup("SudokuFile.txt")
	print (GridToString())
	backTrack()
	print (GridToString())