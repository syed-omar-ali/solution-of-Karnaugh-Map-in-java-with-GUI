import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class Pair{
	int i,j;
	public Pair(int i,int j){
		this.i = i;
		this.j=j;
	}
}
class IO extends JFrame implements ActionListener{
	String input;
	StringBuilder terms,thisTerm;
	Scanner sc;
	Set<Character> totalVariables;
	TextField t;
	JButton b;
	JFrame frame;
	boolean fl;
	public IO(){
		fl=false;
		makeFrame();
		while(fl==false){
			try{
			Thread.sleep(50);
			}catch(Exception e){}
		}
	}
	public void displayMap(boolean[][] map,char[] vocabulary){
		Graphics g = frame.getGraphics();
		int ycor = 200,xcor = 150,side = 50;
		g.drawLine(xcor-side/2, ycor-side/2,xcor-side/2-30, ycor-side/2-30);
		int r = map.length;
		int c = map[0].length;
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		if(r==2){
			g.drawString("0",xcor-side/2-20,ycor+5);
			g.drawString("1",xcor-side/2-20,ycor+5+side);
			g.drawString(new Character(vocabulary[0]).toString(),xcor-side/2-32,ycor-22);
			//g.drawString(new Character(vocabulary[1]).toString(),xcor-side/2-10,ycor-side/2-16);
		}else if(r==4){
			g.drawString("00",xcor-side/2-20,ycor+5);
			g.drawString("01",xcor-side/2-20,ycor+5+side);
			g.drawString("11",xcor-side/2-20,ycor+5+2*side);
			g.drawString("10",xcor-side/2-20,ycor+5+3*side);
			g.drawString(new Character(vocabulary[0]).toString(),xcor-side/2-47,ycor-22);
			g.drawString(new Character(vocabulary[1]).toString(),xcor-side/2-32,ycor-22);
		}
		if(c==2){
			g.drawString("0",xcor-3,ycor-side/2-10);
			g.drawString("1",xcor-3+side,ycor-side/2-10);
			g.drawString(new Character(vocabulary[1]).toString(),xcor-side/2-10,ycor-side/2-16);
		}else if(c==4){
			g.drawString("00",xcor-6,ycor-side/2-10);
			g.drawString("01",xcor-6+side,ycor-side/2-10);
			g.drawString("11",xcor-6+2*side,ycor-side/2-10);
			g.drawString("10",xcor-6+3*side,ycor-side/2-10);
			if(r==2){
				g.drawString(new Character(vocabulary[1]).toString(),xcor-side/2-15,ycor-side/2-16);
				g.drawString(new Character(vocabulary[2]).toString(),xcor-side/2,ycor-side/2-16);
			}else{
				g.drawString(new Character(vocabulary[2]).toString(),xcor-side/2-15,ycor-side/2-16);
				g.drawString(new Character(vocabulary[3]).toString(),xcor-side/2,ycor-side/2-16);
			}
		}
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[i].length;j++){
				g.drawRect (xcor-side/2, ycor-side/2, side,side);
				if(map[i][j])
					g.drawString("1",xcor-5,ycor+5);
				else
					g.drawString("0",xcor-5,ycor+5);
				xcor += side;
			}
			ycor += side;
			xcor = 150;
		}
	}
	public void displaySolution(StringBuilder solution){
		String sol = solution.toString();
		StringBuilder s1 = new StringBuilder();
		for(int i=0;i<sol.length();i++){
			if(i==sol.length()-1)
				continue;
			if(sol.charAt(i)==',')
				s1.append('+');
			else
				s1.append(sol.charAt(i));
		}
		sol = s1.toString();
		TextField f1 = new TextField(100);
		f1.setBounds(100,500,500,25);
		f1.setText("Solution: "+sol);
		f1.setEditable(false);
		frame.add(f1);
	}
	public void makeFrame(){
		frame = new JFrame("Karnaugh Map");
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel l = new JLabel("Enter Expression: ");
		frame.setLayout(null);
		l.setBounds(600-200,20,200,25);
		frame.add(l);
		t = new TextField(50);
		t.setBounds(700-100,20,500,25);
		frame.add(t);
		b = new JButton("Submit");
		b.setBounds(700+500+10-100,20,80,25);
		frame.add(b);
		b.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e){
		input = t.getText();
		thisTerm  = new StringBuilder();
		terms = new StringBuilder();
		totalVariables = new HashSet<Character>();
		for(int i=0;i<input.length();i++){
			if(Character.isLetter(input.charAt(i))){
				thisTerm .append(input.charAt(i));
				totalVariables.add(input.charAt(i));
			}else if(input.charAt(i)=='!'){
				char last = thisTerm.charAt(thisTerm.length()-1);
				thisTerm.setCharAt(thisTerm.length()-1,Character.toLowerCase(last));
			}else if(input.charAt(i)=='+'){
				terms.append(thisTerm+",");
				thisTerm = new StringBuilder();
			}
		}
		terms.append(thisTerm);
		fl=true;
	}
}
class KarnaughMap{
	boolean[][] map;
	String[][] tempMap;
	StringTokenizer tokens;
	String term;
	char[] vocabulary;
	int totalVariablesCount;
	StringBuilder solution;
	ArrayList<Pair> toBeIncluded;
	boolean[][] included;
	ArrayList<String> shorterTerms;
	public boolean[][] buildMap(Set<Character> totalVariables,StringBuilder terms){
		shorterTerms = new ArrayList<String>();
		totalVariablesCount=totalVariables.size();
		int[] numbering = new int[(int)Math.pow(2, totalVariablesCount)];
		if(totalVariablesCount==2){
			int[] seq = {0,1,2,3};
			for(int i=0;i<seq.length;i++)
				numbering[i] = seq[i];
			map = new boolean[2][2];
			tempMap = new String[2][2];
		}else if(totalVariablesCount==3){
			int[] seq = {0,1,3,2,4,5,7,6};
			for(int i=0;i<seq.length;i++)
				numbering[i] = seq[i];
			map = new boolean[2][4];
			tempMap = new String[2][4];
		}else if(totalVariablesCount==4){
			int[] seq = {0,1,3,2,4,5,7,6,12,13,15,14,8,9,11,10};
			for(int i=0;i<seq.length;i++)
				numbering[i] = seq[i];
			map = new boolean[4][4];
			tempMap = new String[4][4];
		}
		Iterator itr = totalVariables.iterator();
		vocabulary = new char[totalVariablesCount];
		int index = 0;
		while(itr.hasNext()){
			vocabulary[index] =((Character)itr.next()).charValue();
			index++;
		}
		index = 0;
		for(int i=0;i<tempMap.length;i++){
			for(int j=0;j<tempMap[i].length;j++){
				tempMap[i][j]  = String.format("%"+totalVariablesCount+"s", Integer.toBinaryString(numbering[index])).replace(' ', '0');
				index++;
			}
		}
		tokens = new StringTokenizer(terms.toString(),",");
		while(tokens.hasMoreTokens()){
			term = tokens.nextToken();
			if(term.length()<vocabulary.length){
				shorterTerms.add(term);
				continue;
			}
			StringBuilder currentTerm = new StringBuilder();
			for(int i=0;i<vocabulary.length;i++){
				if(term.indexOf(vocabulary[i])>=0){
					currentTerm.append("1");
				}else if(term.indexOf(Character.toLowerCase(vocabulary[i]))>=0){
					currentTerm.append("0");
				}
			}
			String currentTermString = currentTerm.toString();
			for(int i=0;i<tempMap.length;i++){
				for(int j=0;j<tempMap[i].length;j++){
					if(tempMap[i][j].equals(currentTermString)){
						map[i][j] = true;
					}
				}
			}
		}
		 itr = shorterTerms.iterator();
		while(itr.hasNext()){
			String s = itr.next().toString();
			StringBuilder temps = new StringBuilder();
			for(int i=0;i<vocabulary.length;i++){
				if(s.indexOf(vocabulary[i])>=0)
					temps.append('1');
				else if(s.indexOf(Character.toLowerCase(vocabulary[i]))>=0)
					temps.append('0');
				else
					temps.append('N');

			}
			for(int i=0;i<tempMap.length;i++){
				for(int j=0;j<tempMap[i].length;j++){
					int f = 1;
					for(int k=0;k<vocabulary.length;k++){
						if(temps.charAt(k)!='N' && temps.charAt(k)!=tempMap[i][j].charAt(k)){
							f=0;
							break;
						}
					}
					if(f==1)
						map[i][j] = true;
				}
			}

		}
		return map;
	}
	public void markIncluded(){
		Iterator itr = toBeIncluded.iterator();
		while(itr.hasNext()){
			Pair p = (Pair)itr.next();
			included[p.i][p.j] = true;
		}
	}
	public StringBuilder solveKMap(){
		boolean flag = true;
		included = new boolean[map.length][map[0].length];
		solution = new StringBuilder();
		toBeIncluded = new ArrayList<Pair> ();
		if(totalVariablesCount==2){	// 2 VARIABLE CASE
			outer:
			//checking for square of 4 cells
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(!map[i][j]){
						flag = false;
						break outer;
					}
				}
			}
			if(flag){
				solution.append("1");
				return solution;
			}
			// checking all combinations of groups of 2
			//HORIZONTAL
			for(int i=0;i<2;i++){
				if(map[i][0] && map[i][1]  && !included[i][0] && !included[i][1]){
					toBeIncluded.add(new Pair(i,0));
					toBeIncluded.add(new Pair(i,1));
					solution.append(new Character(vocabulary[0]).toString());
					if(i==0)
						solution.append("!");
					solution.append(",");
				}
			}
			//VERTICAL
			for(int i=0;i<2;i++){
				if(map[0][i] && map[1][i]  && !included[0][i] && !included[1][i]){
					toBeIncluded.add(new Pair(0,i));
					toBeIncluded.add(new Pair(1,i));
					solution.append(new Character(vocabulary[1]).toString());
					if(i==0)
						solution.append("!");
					solution.append(",");
				}
			}
			markIncluded();
			// checking if single points are left out
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(map[i][j] && !included[i][j]){
						if(map[(i+1)%2][j]){
							solution.append(new Character(vocabulary[1]).toString());
							if(j==0)
								solution.append("!");
							toBeIncluded.add(new Pair(i,j));
							solution.append(",");
						}else if(map[i][(j+1)%2]){
							solution.append(new Character(vocabulary[0]).toString());
							if(i==0)
								solution.append("!");
							toBeIncluded.add(new Pair(i,j));
							solution.append(",");
						}
						
					}
				}
			}
			markIncluded();
			// checking for diagonal points
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(map[i][j] && !included[i][j]){
						solution.append(new Character(vocabulary[0]).toString());
						if(i==0)
							solution.append("!");
						solution.append(new Character(vocabulary[1]).toString());
						if(j==0)
							solution.append("!");
						toBeIncluded.add(new Pair(i,j));
						solution.append(",");
					}
				}
			}
			markIncluded();
		}else if(totalVariablesCount==3){	// 3 VARIABLE CASE
			outer:
			//checking for square of 8 cells
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(!map[i][j]){
						flag = false;
						break outer;
					}
				}
			}
			if(flag){
				solution.append("1");
				return solution;
			}
			// checking all combinations of groups of 4
			//HORIZONTAL
			for(int j=0;j<4;j++){
				if(map[0][j] && map[0][(j+1)%4] && map[1][j] && map[1][(j+1)%4] && !included[0][j] && !included[0][(j+1)%4] && !included[1][j] && !included[1][(j+1)%4] ){
					toBeIncluded.add(new Pair(0,j));
					toBeIncluded.add(new Pair(0,(j+1)%4));
					toBeIncluded.add(new Pair(1,j));
					toBeIncluded.add(new Pair(1,(j+1)%4));
					if(j%2==0)
						solution.append(new Character(vocabulary[1]).toString());
					else
						solution.append(new Character(vocabulary[2]).toString());
					if(j==0 || j==3)
						solution.append("!");
					solution.append(",");
				}
			}
			//VERTICAL
			for(int i=0;i<2;i++){
				if(map[i][0] && map[i][1] && map[i][2] && map[i][3] && !included[i][0] && !included[i][1] && !included[i][2] && !included[i][3]){
					toBeIncluded.add(new Pair(i,0));
					toBeIncluded.add(new Pair(i,1));
					toBeIncluded.add(new Pair(i,2));
					toBeIncluded.add(new Pair(i,3));
					solution.append(new Character(vocabulary[0]).toString());
					if(i==0)
						solution.append("!");
					solution.append(",");
				}
			}
			markIncluded();
			// checking all combinations of groups of 2
			//HORIZONTAL
			for(int i=0;i<2;i++){
				for(int j=0;j<4;j++){
					if(map[i][j] && map[i][(j+1)%4] && !included[i][j] && !included[i][(j+1)%4] ){
						toBeIncluded.add(new Pair(i,j));
						toBeIncluded.add(new Pair(i,(j+1)%4));
						solution.append(new Character(vocabulary[0]).toString());
						if(i==0)
							solution.append("!");
						if(j%2==0)
							solution.append(new Character(vocabulary[1]).toString());
						else
							solution.append(new Character(vocabulary[2]).toString());
						if(j==0 || j==3)
							solution.append("!");
						solution.append(",");
					}
				}
			}
			//VERTICAL
			for(int j=0;j<4;j++){
				if(map[0][j] && map[1][j] && !included[0][j] && !included[1][j]){
					toBeIncluded.add(new Pair(0,j));
					toBeIncluded.add(new Pair(1,j));
					solution.append(new Character(vocabulary[1]).toString());
					if(j==0 || j==1)
						solution.append("!");
					solution.append(new Character(vocabulary[2]).toString());
					if(j==0 || j==3)
						solution.append("!");
					solution.append(",");
				}
			}
			markIncluded();
			// checking for single points 
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(map[i][j] && !included[i][j]){
						if(map[(i+1)%2][j]){
							toBeIncluded.add(new Pair(i,j));
							solution.append(new Character(vocabulary[1]).toString());
							if(j==0 || j==1)
								solution.append("!");
							solution.append(new Character(vocabulary[2]).toString());
							if(j==0 || j==3)
								solution.append("!");
							solution.append(",");
						}else if(map[i][(j+1)%4]){
							solution.append(new Character(vocabulary[0]).toString());
							if(i==0)
								solution.append("!");
							toBeIncluded.add(new Pair(i,j));
							if(j%2==0)
								solution.append(new Character(vocabulary[1]).toString());
							else
								solution.append(new Character(vocabulary[2]).toString());
							if(j==0 || j==3)
								solution.append("!");
							solution.append(",");
						}else if(map[i][(j-1+4)%4]){
							solution.append(new Character(vocabulary[0]).toString());
							if(i==0)
								solution.append("!");
							toBeIncluded.add(new Pair(i,j));
							j = (j-1+4)%4;
							if(j%2==0)
								solution.append(new Character(vocabulary[1]).toString());
							else
								solution.append(new Character(vocabulary[2]).toString());
							if(j==0 || j==3)
								solution.append("!");
							solution.append(",");
						}
						
					}
				}
			}
			markIncluded();
			// If still single points could not group to anyone
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(map[i][j] && !included[i][j]){
						toBeIncluded.add(new Pair(i,j));
						solution.append(new Character(vocabulary[0]).toString());
						if(i==0)
							solution.append("!");
						solution.append(new Character(vocabulary[1]).toString());
						if(j==0 || j==1)
							solution.append("!");
						solution.append(new Character(vocabulary[2]).toString());
						if(j==0 || j==3)
							solution.append("!");
						solution.append(",");
					}
				}
			}
			markIncluded();
		}else if(totalVariablesCount==4){	// 4 VARIABLE CASE
			outer:
			//checking for square of 16  cells
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(!map[i][j]){
						flag = false;
						break outer;
					}
				}
			}
			if(flag){
				solution.append("1");
				return solution;
			}
			// checking all combinations of groups of 8
			//HORIZONTAL
			for(int j=0;j<4;j++){
				if(map[0][j] && map[0][(j+1)%4] && map[1][j] && map[1][(j+1)%4] && map[2][j]  && map[2][(j+1)%4]  && map[3][j] && map[3][(j+1)%4] && !included[0][j] && !included[0][(j+1)%4] && !included[1][j] && !included[1][(j+1)%4] && !included[2][j]  && !included[2][(j+1)%4]  && !included[3][j] && !included[3][(j+1)%4]){
					 toBeIncluded.add(new Pair(0,j));
					 toBeIncluded.add(new Pair(0,(j+1)%4));
					 toBeIncluded.add(new Pair(1,j));
					 toBeIncluded.add(new Pair(1,(j+1)%4));
					 toBeIncluded.add(new Pair(2,j));
					 toBeIncluded.add(new Pair(2,(j+1)%4));
					 toBeIncluded.add(new Pair(3,j));
					 toBeIncluded.add(new Pair(3,(j+1)%4));
					if(j%2==0)
						solution.append(new Character(vocabulary[2]).toString());
					else
						solution.append(new Character(vocabulary[3]).toString());
					if(j==0 || j==3)
						solution.append("!");
					solution.append(",");
				}
			}
			
			//VERTICAL
			for(int i=0;i<4;i++){
				if(map[i][0] && map[i][1] && map[i][2] && map[i][3]&& map[(i+1)%4][0] && map[(i+1)%4][1] && map[(i+1)%4][2] && map[(i+1)%4][3] && !included[i][0] && !included[i][1] && !included[i][2] && !included[i][3] && !included[(i+1)%4][0] && !included[(i+1)%4][1] && !included[(i+1)%4][2] && !included[(i+1)%4][3]){
					toBeIncluded.add(new Pair(i,0));
					toBeIncluded.add(new Pair(i,1));
					toBeIncluded.add(new Pair(i,2));
					toBeIncluded.add(new Pair(i,3));
					toBeIncluded.add(new Pair((i+1)%4,0));
					toBeIncluded.add(new Pair((i+1)%4,1));
					toBeIncluded.add(new Pair((i+1)%4,2));
					toBeIncluded.add(new Pair((i+1)%4,3));
					if(i%2==0)
						solution.append(new Character(vocabulary[0]).toString());
					else
						solution.append(new Character(vocabulary[1]).toString());
					if(i==0 || i==3)
						solution.append("!");
					solution.append(",");
				}
			}
			markIncluded();
			// checking all combinations of groups of 4
			//BLOCKS
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					if(map[i][j] && map[(i+1)%4][j] && map[i][(j+1)%4] && map[(i+1)%4][(j+1)%4] && !included[i][j] && !included[(i+1)%4][j] && !included[i][(j+1)%4] && !included[(i+1)%4][(j+1)%4]){
						toBeIncluded.add(new Pair(i,j));
						toBeIncluded.add(new Pair((i+1)%4,j));
						toBeIncluded.add(new Pair(i,(j+1)%4));
						toBeIncluded.add(new Pair((i+1)%4,(j+1)%4));
						if(i%2==0)
							solution.append(new Character(vocabulary[0]).toString());
						else
							solution.append(new Character(vocabulary[1]).toString());
						if(i==0 || i==3)
							solution.append("!");
						if(j%2==0)
							solution.append(new Character(vocabulary[2]).toString());
						else
							solution.append(new Character(vocabulary[3]).toString());
						if(j==0 || j==3)
							solution.append("!");
						solution.append(",");
					}
				}
			}
			//HORIZONTAL
			for(int i=0;i<4;i++){
				if(map[i][0] && map[i][1] && map[i][2] && map[i][3] && !included[i][0] && !included[i][1] && !included[i][2] && !included[i][3]){
					solution.append(new Character(vocabulary[0]).toString());
					if(i==0 || i==1)
						solution.append("!");
					solution.append(new Character(vocabulary[1]).toString());
					if(i==0 || i==3)
						solution.append("!");
					solution.append(",");
					toBeIncluded.add(new Pair(i,0));
					toBeIncluded.add(new Pair(i,1));
					toBeIncluded.add(new Pair(i,2));
					toBeIncluded.add(new Pair(i,3));
				}
			}
			//VERTICAL
			for(int j=0;j<4;j++){
				if(map[0][j] && map[1][j] && map[2][j] && map[3][j] && !included[0][j] && !included[1][j] && !included[2][j] && !included[3][j]){
					solution.append(new Character(vocabulary[2]).toString());
					if(j==0 || j==1)
						solution.append("!");
					solution.append(new Character(vocabulary[3]).toString());
					if(j==0 || j==3)
						solution.append("!");
					solution.append(",");
					toBeIncluded.add(new Pair(0,j));
					toBeIncluded.add(new Pair(1,j));
					toBeIncluded.add(new Pair(2,j));
					toBeIncluded.add(new Pair(3,j));
				}
			}
			markIncluded();
			// checking all combinations of groups of 2
			//HORIZONTAL
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					if(map[i][j] && map[i][(j+1)%4] && !included[i][j] && !included[i][(j+1)%4]){
						toBeIncluded.add(new Pair(i,j));
						toBeIncluded.add(new Pair(i,(j+1)%4));
						solution.append(new Character(vocabulary[0]).toString());
						if(i==0 || i==1)
							solution.append("!");
						solution.append(new Character(vocabulary[1]).toString());
						if(i==0 || i==3)
							solution.append("!");
						if(j%2==0)
							solution.append(new Character(vocabulary[2]).toString());
						else
							solution.append(new Character(vocabulary[3]).toString());
						if(j==0 || j==3)
							solution.append("!");
						solution.append(",");
					}
				}
			}
			//VERTICAL
			for(int j=0;j<4;j++){
				for(int i=0;i<4;i++){
					if(map[i][j] && map[(i+1)%4][j] && !included[i][j] && !included[(i+1)%4][j]){
						toBeIncluded.add(new Pair(i,j));
						toBeIncluded.add(new Pair((i+1)%4,j));
						if(i%2==0)
							solution.append(new Character(vocabulary[0]).toString());
						else
							solution.append(new Character(vocabulary[1]).toString());
						if(i==0 || i==3)
							solution.append("!");
						solution.append(new Character(vocabulary[2]).toString());
						if(j==0 || j==1)
							solution.append("!");
						solution.append(new Character(vocabulary[3]).toString());
						if(j==0 || j==3)
							solution.append("!");
						solution.append(",");
					}
				}
			}
			markIncluded();
			// checking for single points 
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(map[i][j] && !included[i][j]){
						if(map[i][(j+1)%4]){
							included[i][j] = true;
							solution.append(new Character(vocabulary[0]).toString());
							if(i==0 || i==1)
								solution.append("!");
							solution.append(new Character(vocabulary[1]).toString());
							if(i==0 || i==3)
								solution.append("!");
							if(j%2==0)
								solution.append(new Character(vocabulary[2]).toString());
							else
								solution.append(new Character(vocabulary[3]).toString());
							if(j==0 || j==3)
								solution.append("!");
							solution.append(",");
						}else if(map[i][(j-1+4)%4]){
							included[i][j] = true;
							j = (j-1+4)%4;
							solution.append(new Character(vocabulary[0]).toString());
							if(i==0 || i==1)
								solution.append("!");
							solution.append(new Character(vocabulary[1]).toString());
							if(i==0 || i==3)
								solution.append("!");
							if(j%2==0)
								solution.append(new Character(vocabulary[2]).toString());
							else
								solution.append(new Character(vocabulary[3]).toString());
							if(j==0 || j==3)
								solution.append("!");
							solution.append(",");
						}else if(map[(i+1)%4][j]){
							included[i][j] = true;
							if(i%2==0)
								solution.append(new Character(vocabulary[0]).toString());
							else
								solution.append(new Character(vocabulary[1]).toString());
							if(i==0 || i==3)
								solution.append("!");
							solution.append(new Character(vocabulary[2]).toString());
							if(j==0 || j==1)
								solution.append("!");
							solution.append(new Character(vocabulary[3]).toString());
							if(j==0 || j==3)
								solution.append("!");
							solution.append(",");
						}else if(map[(i-1+4)%4][j]){
							included[i][j] = true;
							i = (i-1+4)%4 ;
							if(i%2==0)
								solution.append(new Character(vocabulary[0]).toString());
							else
								solution.append(new Character(vocabulary[1]).toString());
							if(i==0 || i==3)
								solution.append("!");
							solution.append(new Character(vocabulary[2]).toString());
							if(j==0 || j==1)
								solution.append("!");
							solution.append(new Character(vocabulary[3]).toString());
							if(j==0 || j==3)
								solution.append("!");
							solution.append(",");
						}
						
					}
				}
			}
			// If still single points could not group to anyone
			for(int i=0;i<map.length;i++){
				for(int j=0;j<map[i].length;j++){
					if(map[i][j] && !included[i][j]){
						included[i][j] = true;
						solution.append(new Character(vocabulary[0]).toString());
						if(i==0 || i==1)
							solution.append("!");
						solution.append(new Character(vocabulary[1]).toString());
						if(i==0 || i==3)
							solution.append("!");
						solution.append(new Character(vocabulary[2]).toString());
						if(j==0 || j==1)
							solution.append("!");
						solution.append(new Character(vocabulary[3]).toString());
						if(j==0 || j==3)
							solution.append("!");
						solution.append(",");
					}
				}
			}	
		}
		return solution;
	}
}
class KMapUTIL{
	public KMapUTIL(){
		IO io = new IO();
		Set<Character> totalVariables = io.totalVariables;
		StringBuilder terms = io.terms;
		KarnaughMap kMap = new KarnaughMap();
		boolean[][] map = kMap.buildMap(totalVariables,terms);
		char[] vocabulary = kMap.vocabulary;
		io.displayMap(map,vocabulary);
		StringBuilder solution = kMap.solveKMap();
		io.displaySolution(solution);
	}
}
class P5{
	public static void main(String[] args) {
		new KMapUTIL();
	}
}