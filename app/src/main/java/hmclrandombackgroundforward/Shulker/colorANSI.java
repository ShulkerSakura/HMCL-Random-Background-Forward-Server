package hmclrandombackgroundforward.Shulker;

public class colorANSI{
	public String color(String col){
		String r,g,b,y,p,c,w,reset,bold,underline;
		r = "\033[31m";
		g =	"\033[32m";
		y = "\033[33m";
		b = "\033[34m";
		p = "\033[35m";
		c = "\033[36m";
		w = "\033[37m";
		reset = "\033[0m";
		bold = "\033[1m";
		underline = "\033[4m";
		switch (col) {
			case "red":
				return r;
			case "green":
				return g;
			case "yellow":
				return y;
			case "blue":
				return b;
			case "purple":
				return p;
			case "cyan":
				return c;
			case "white":
				return w;
			case "reset":
				return reset;
			case "bold":
				return bold;
			case "underline":
                return underline;
			default:
				return "";
		}
	}
	public void testColor(){
		System.out.println(color("red")+"red");
		System.out.println(color("green")+"green");
		System.out.println(color("yellow")+"yellow");
		System.out.println(color("blue")+"blue");
		System.out.println(color("purple")+"purple");
		System.out.println(color("cyan")+"cyan");
		System.out.println(color("white")+"white");
		System.out.println(color("bold")+"bold");
		System.out.println(color("underline")+"underline"+color("reset"));
	}
}
