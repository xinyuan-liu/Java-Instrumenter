package Tracor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class TraceParser {

	private static class Variable implements Cloneable, Comparable {
		String Name;
		String Type;
		Object Value;
		Boolean Defined;

		Variable() {
		}

		Variable(String _Name, String _Type, Object _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		public Object clone() {
			Variable o = null;
			try {
				o = (Variable) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return o;
		}

		@Override
		public String toString() {
			return "Variable [Name=" + Name + ", Type=" + Type + ", Value=" + Value + ", Defined=" + Defined + "]";
		}

		@Override
		public int compareTo(Object arg0) {
			if (arg0 instanceof Variable) {
				return Name.compareTo(((Variable) arg0).Name);
			}
			return 0;
		}
	}

	private static class VarInt extends Variable {
		int Value;

		VarInt(String _Name, String _Type, int _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarInt [Name=" + Name + ", Value=" + Value + "]";
		}
	}

	private static class VarLong extends Variable {
		long Value;

		VarLong(String _Name, String _Type, long _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarLong [Name=" + Name + ", Value=" + Value + "]";
		}
	}
	
	private static class VarByte extends Variable {
		byte Value;

		VarByte(String _Name, String _Type, Byte _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarByte [Name=" + Name + ", Value=" + Value + "]";
		}
	}
	
	private static class VarString extends Variable {
		String Value;

		VarString(String _Name, String _Type, String _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarString [Name=" + Name + ", Value=" + Value + "]";
		}
	}

	private static class VarShort extends Variable {
		short Value;

		VarShort(String _Name, String _Type, short _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarShort [Name=" + Name + ", Value=" + Value + "]";
		}
	}

	private static class VarChar extends Variable {
		char Value;

		VarChar(String _Name, String _Type, char _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarChar [Name=" + Name + ", Value=" + Value + "]";
		}
	}

	private static class VarFloat extends Variable {
		float Value;

		VarFloat(String _Name, String _Type, float _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarFloat [Name=" + Name + ", Value=" + Value + "]";
		}
	}

	private static class VarDouble extends Variable {
		double Value;

		VarDouble(String _Name, String _Type, double _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarDouble [Name=" + Name + ", Value=" + Value + "]";
		}
	}

	private static class VarBoolean extends Variable {
		boolean Value;

		VarBoolean(String _Name, String _Type, boolean _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		@Override
		public String toString() {
			return "VarBoolean [Name=" + Name + ", Value=" + Value + "]";
		}
	}

	private static class VarObject extends Variable {
		VarObject(String _Name, String _Type, Object _Value) {
			Name = _Name;
			Type = _Type;
			Value = _Value;
		}

		public String toString() {
			return "VarObect [Name=" + Name + "]";
		}
	}

	private static class Statement {
		String StmtType;
		String Filename;

		void set(String file, String type) {
			Filename = file;
			StmtType = type;
		}

	}

	private static class IfStatement extends Statement {
		boolean taken;// 0 for reached, 1 for taken
		int startLine;
		int endLine;
		boolean hasElse;
		int elseStartLine;
		int elseEndLine;

		IfStatement(boolean _taken, int _startLine, int _endLine) {
			taken = _taken;
			startLine = _startLine;
			endLine = _endLine;
		}

		@Override
		public String toString() {
			return "IfStatement [taken=" + taken + ", startLine=" + startLine + ", endLine=" + endLine + ", hasElse="
					+ hasElse + ", elseStartLine=" + elseStartLine + ", elseEndLine=" + elseEndLine + "]";
		}
	}

	private static class WhileStatement extends Statement {
		boolean taken;// 0 for reached, 1 for taken
		int startLine;
		int endLine;
		boolean firsttaken;
		WhileStatement(boolean _taken, int _startLine, int _endLine) {
			taken = _taken;
			startLine = _startLine;
			endLine = _endLine;
			firsttaken=false;
		}

		@Override
		public String toString() {
			return "WhileStatement [taken=" + taken + ", startLine=" + startLine + ", endLine=" + endLine + "]";
		}
	}

	private static class DoStatement extends Statement {
		boolean taken;// 0 for reached, 1 for taken
		int startLine;
		int endLine;

		DoStatement(boolean _taken, int _startLine, int _endLine) {
			taken = _taken;
			startLine = _startLine;
			endLine = _endLine;
		}

		@Override
		public String toString() {
			return "DoStatement [taken=" + taken + ", startLine=" + startLine + ", endLine=" + endLine + "]";
		}
	}
	
	private static class VariableDeclaration extends Statement {
		Variable var;
		int Line;

		VariableDeclaration(Variable _var, int _Line) {
			var = _var;
			Line = _Line;
		}

		@Override
		public String toString() {
			return "VariableDeclaration [var=" + var + ", Line=" + Line + "]";
		}
	}

	private static class MethodInvoked extends Statement {
		String MethodName;
		Set<Variable> Parameters;
		int Line;

		MethodInvoked(String _MethodName, Set<Variable> _Parameters, int _Line) {
			MethodName = _MethodName;
			Parameters = _Parameters;
			Line = _Line;
		}

		@Override
		public String toString() {
			return "MethodInvoked [MethodName=" + MethodName + ", Parameters=" + Parameters + ", Line=" + Line + "]";
		}
	}

	private static class Assignment extends Statement {
		Variable var;
		int Line;

		Assignment(Variable _var, int _Line) {
			var = _var;
			Line = _Line;
		}

		@Override
		public String toString() {
			return "Assignment [var=" + var + ", Line=" + Line + "]";
		}
	}

	private static class ReturnStatement extends Statement {
		// TODO : return value
		Variable var;
		int Line;

		ReturnStatement(Variable _var, int _Line) {
			var = _var;
			Line = _Line;
		}

		@Override
		public String toString() {
			return "ReturnStatement [var=" + var + ", Line=" + Line + "]";
		}
	}

	private static class LineVariables {
		public LineVariables(int line, Set<Variable> variables) {
			super();
			this.line = line;
			Variables = variables;
		}

		int line;
		Set<Variable> Variables;

		@Override
		public String toString() {
			return "LineVariables [line=" + line + ", Variables=" + Variables + "]";
		}
	}

	private static class Jump {
		public Jump(int fromline, int toline) {
			super();
			this.fromline = fromline;
			this.toline = toline;
		}

		int fromline;
		int toline;

	}

	private static class Spectrum {
		List<LineVariables> values;
		Queue<Jump> pendingjumps;
		int curLine;

		void runto(int targetline) {
			LineVariables last = values.get(values.size() - 1);
			do {
				if ((pendingjumps.isEmpty() || curLine > pendingjumps.peek().fromline) && curLine > targetline) {
					break;
					// throw new Exception("wild line");
				}
				if (!pendingjumps.isEmpty() && pendingjumps.peek().fromline == curLine) {
					curLine = pendingjumps.poll().toline;
				} else {
					curLine++;
				}
				values.add(new LineVariables(curLine, last.Variables));
			} while (curLine != targetline);
		}

		Spectrum() {
			values = new ArrayList<LineVariables>();
			pendingjumps = new LinkedList<Jump>();
		}

		void form(List<Statement> Stmts) {
			values = new ArrayList<LineVariables>();
			pendingjumps = new LinkedList<Jump>();
			Iterator<Statement> it = Stmts.iterator();
			while (it.hasNext()) {
				Statement st = it.next();

				if (st instanceof IfStatement) {
					int t = ((IfStatement) st).startLine;
					runto(t);
					if (!((IfStatement) st).taken) {
						if (!((IfStatement) st).hasElse)
							pendingjumps.offer(new Jump(curLine, ((IfStatement) st).endLine));
						// curLine = ((IfStatement) st).endLine;
						else {
							pendingjumps.offer(new Jump(curLine, ((IfStatement) st).elseStartLine));
							// curLine = ((IfStatement) st).elseStartLine;
						}
					} else {
						pendingjumps.offer(new Jump(curLine, ((IfStatement) st).startLine));
						// curLine=(((IfStatement) st).startLine);
					}
				}
				if (st instanceof WhileStatement) {
					int t = ((WhileStatement) st).startLine;
					
					
					if (!((WhileStatement) st).taken)
					{
						pendingjumps.offer(new Jump(curLine, ((WhileStatement) st).endLine));
						runto(t);
					}
					else {
						if(((WhileStatement) st).firsttaken)
						{
							runto(t);
						}
						else
						{
							pendingjumps.offer(new Jump(((WhileStatement) st).endLine, ((WhileStatement) st).startLine));
							runto(t);
						}
							
					}
						
					// curLine=((WhileStatement) st).startLine;
					
				}
				
				if (st instanceof VariableDeclaration) {
					int t = ((VariableDeclaration) st).Line;
					runto(t);
					Set<Variable> tmp = new TreeSet<Variable>();
					tmp.addAll(values.get(values.size() - 1).Variables);
					tmp.add(((VariableDeclaration) st).var);
					values.remove(values.size() - 1);
					values.add(new LineVariables(t, tmp));
				}
				if (st instanceof MethodInvoked) {
					int t = ((MethodInvoked) st).Line;
					// pendingjumps.offer(new Jump(curLine,t));
					curLine = t;
					Set<Variable> tmp = new TreeSet<Variable>();
					tmp.addAll(((MethodInvoked) st).Parameters);
					values.add(new LineVariables(t, tmp));
				}
				if (st instanceof Assignment) {
					int t = ((Assignment) st).Line;
					runto(t);
					LineVariables tmp = values.get(values.size() - 1);
					Set<Variable> add = new TreeSet<Variable>();
					String s = ((Assignment) st).var.Name;
					boolean flag=true;
					for (Variable v : tmp.Variables) {
						if (v.Name == s) {
							flag=false;
							add.add(((Assignment) st).var);
						} else
							add.add(v);
					}
					if(flag) {
						add.add(((Assignment) st).var);
					}
					values.remove(values.size() - 1);
					values.add(new LineVariables(t, add));
					curLine = t;
				}
				if (st instanceof ReturnStatement) {
					int t = ((ReturnStatement) st).Line;
					runto(t);
					// TODO
				}
				// System.out.println(values);
				/*
				 * System.out.println(st); System.out.println(curLine); for
				 * (LineVariables i : values) { System.out.println("Line " +
				 * i.line + ": " + i.Variables); }
				 */
			}
			for (LineVariables i : values) {
				// if(verbose)
				// System.out.println("Line " + i.line + ": " + i.Variables);
			}
		}

		public static class Mode {
			public enum ModeEnum {
				Default, LCS, LCS_Bestfit;
			}

			ModeEnum mode;
			double varw;
			double sizediffw;
			double linediffw;

			Mode(ModeEnum _mode, double _varw, double _sizediffw, double _linediffw) {
				mode = _mode;
				varw = _varw;
				sizediffw = _sizediffw;
				linediffw = _linediffw;
			}
		}

		
	}

	public static int getLine(String s) {
		return Integer.parseInt(s.substring(s.indexOf(":") + 1));
	}

	public static String getFile(String s) {
		return s.substring(s.indexOf(":") + 1);
	}

	public static void getElseLines(String t, IfStatement st) {
		if (t.startsWith(" "))
			t = t.substring(1);
		// System.out.println("getElseLines from \"" + t + "\"");
		if (t.equals("Else:null"))
			st.hasElse = false;
		else {
			st.hasElse = true;
			st.elseStartLine = Integer.parseInt(t.substring(t.indexOf(":") + 1, t.indexOf(" ")));
			st.elseEndLine = Integer.parseInt(t.substring(t.indexOf(" ", t.indexOf(" ") + 1) + 1));
		}
	}

	public static void getBranchLines(String t, IfStatement st) {
		if (t.startsWith(" "))
			t = t.substring(1);
		// System.out.println("getBranchLines from \"" + t + "\"");
		st.startLine = Integer.parseInt(t.substring(t.indexOf(":") + 1, t.indexOf(" ")));
		st.endLine = Integer.parseInt(t.substring(t.indexOf(" ", t.indexOf(" ") + 1) + 1));
	}

	public static void getBranchLines(String t, WhileStatement st) {
		if (t.startsWith(" "))
			t = t.substring(1);
		// System.out.println("getBranchLines from \"" + t + "\"");
		st.startLine = Integer.parseInt(t.substring(t.indexOf(":") + 1, t.indexOf(" ")));
		st.endLine = Integer.parseInt(t.substring(t.indexOf(" ", t.indexOf(" ") + 1) + 1));
	}

	public static Variable getVariable(Scanner sc) {
		String t1 = sc.next();
		String t2 = sc.next();
		// System.out.println("t1"+t1+"t2"+t2);
		String type = t2.substring(t2.indexOf(':') + 1);
		Scanner sc2 = new Scanner(t1).useDelimiter("=");
		Variable ret = null;
		String name;
		Object value;
		switch (type) {
		case "int":
			name = sc2.next();
			value = sc2.nextInt();
			ret = new VarInt(name, type, (int) value);
			ret.Defined = true;
			break;
		case "long":
			name = sc2.next();
			value = sc2.nextLong();
			ret = new VarLong(name, type, (long) value);
			ret.Defined = true;
			break;
		case "byte":
			name = sc2.next();
			value = sc2.nextByte();
			ret = new VarByte(name, type, (byte) value);
			ret.Defined = true;
			break;
		case "short":
			name = sc2.next();
			value = sc2.nextShort();
			ret = new VarShort(name, type, (short) value);
			ret.Defined = true;
			break;
		case "Object":
			name = sc2.next();
			value = sc2.next();
			ret = new VarObject(name, type, value);
			ret.Defined = true;
			break;
		case "char":
			name = sc2.next();
			value = sc2.next().charAt(0);
			ret = new VarChar(name, type, (char) value);
			ret.Defined = true;
			break;
		case "boolean":
			name = sc2.next();
			value = sc2.nextBoolean();
			ret = new VarBoolean(name, type, (boolean) value);
			ret.Defined = true;
			break;
		case "float":
			name = sc2.next();
			value = sc2.nextFloat();
			ret = new VarFloat(name, type, (float) value);
			ret.Defined = true;
			break;
		case "double":
			name = sc2.next();
			value = sc2.nextDouble();
			ret = new VarDouble(name, type, (double) value);
			ret.Defined = true;
			break;
		case "string":
			name = sc2.next();
			value = sc2.next();
			ret = new VarString(name, type, (String) value);
			ret.Defined = true;
			break;
		}
		sc2.close();
		// System.out.println(ret);
		return ret;
	}

	public static Statement getStatement(String src) {
		Scanner sc = new Scanner(src);
		String label = sc.next();
		Scanner labelsc = new Scanner(label.substring(1, label.indexOf(">"))).useDelimiter(",");
		String type = labelsc.next();
		//System.out.println(src);
		Statement ret = null;
		String file = null;
		// System.out.println(type);
		switch (type) {
		case "Method_invoked":
			String funcname = labelsc.next();
			int parac = labelsc.nextInt();
			labelsc.close();
			labelsc = new Scanner(sc.next()).useDelimiter(",");
			Set<Variable> Parameters = new TreeSet<Variable>();
			for (int i = 0; i < parac; i++) {
				Variable par = getVariable(labelsc);
				Parameters.add(par);
			}
			int line = getLine(labelsc.next());
			file = getFile(labelsc.next());
			ret = new MethodInvoked(funcname, Parameters, line);
			break;
		case "IfStatement":
			String temp = labelsc.next();
			boolean taken = (temp.equals("taken") ? true : false);
			sc.useDelimiter(",");
			ret = new IfStatement(taken, 0, 0);
			getBranchLines(sc.next(), (IfStatement) ret);
			getElseLines(sc.next(), (IfStatement) ret);
			file = getFile(sc.next());
			break;
		case "WhileStatement":
			temp = labelsc.next();
			taken = (temp.equals("taken") ? true : false);
			ret = new WhileStatement(taken, 0, 0);
			sc.useDelimiter(",");
			getBranchLines(sc.next(), (WhileStatement) ret);
			file = getFile(sc.next());

			break;
		case "Assignment":
			labelsc.close();
			labelsc = new Scanner(sc.next()).useDelimiter(",");
			Variable var = getVariable(labelsc);
			line = getLine(labelsc.next());
			file = getFile(labelsc.next());
			ret = new Assignment(var, line);
			break;
		case "ReturnStatement":
			labelsc.close();
			labelsc = new Scanner(sc.next()).useDelimiter(",");
			var = getVariable(labelsc);
			line = getLine(labelsc.next());
			file = getFile(labelsc.next());
			ret = new ReturnStatement(var, line);
			break;
		case "VariableDeclaration":
			labelsc.close();
			labelsc = new Scanner(sc.next()).useDelimiter(",");
			var = getVariable(labelsc);
			line = getLine(labelsc.next());
			file = getFile(labelsc.next());
			ret = new VariableDeclaration(var, line);
			break;
		}
		labelsc.close();
		sc.close();

		ret.set(file, type);
		// System.out.println(ret);
		return ret;
	}

	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}

	public static List<Statement> parsetrace(String Filename) throws IOException {
		List<Statement> Stmts = new ArrayList<Statement>();
		BufferedReader reader = new BufferedReader(new FileReader(Filename));
		String str = null;
		while ((str = reader.readLine()) != null) {
			Statement st = getStatement(str);
			if (st instanceof IfStatement) {
				if (((IfStatement) st).taken) {
					Stmts.remove(Stmts.size() - 1);
				}
			}
			if (st instanceof WhileStatement) {
				if (((WhileStatement) st).taken) {
					Statement s=Stmts.get(Stmts.size()-1);
					if(s instanceof WhileStatement)
					{
						if(((WhileStatement) s).startLine==((WhileStatement)st).startLine && !((WhileStatement) s).taken)
						{
							Stmts.remove(Stmts.size() - 1);
							((WhileStatement) st).firsttaken=true;
						}
					}
					
					
				}
			}
			Stmts.add(st);
		}
		for (Statement st : Stmts) {
			// System.out.println(st);
		}
		return Stmts;
	}

	public static void main(String args[]) {
		String TraceFile = "/Users/liuxinyuan/DefectRepairing/a.txt";
		
		
		Spectrum spec = new Spectrum();
		try {
			spec.form(parsetrace(TraceFile));
		} catch (IOException e) {
			System.out.println("parse Tracefile1 failed");
			e.printStackTrace();
		}
		for(LineVariables lv: spec.values)
		{
			System.out.println (lv);
		}
		
		
	}

}
