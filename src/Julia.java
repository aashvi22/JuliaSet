
public class Julia {
	int a,b, multiplier, hue,zoom;
	public Julia(int a, int b, int m, int hue, int zoom) {
		this.a = a;
		this.b=b;
		this.multiplier=m;
		this.hue=hue;
		this.zoom=zoom;
	}
	public int[] getValues() {
						//0, 1, 2,         3,   4
		int[]values = {a,b, multiplier, hue,zoom};
		return values;
	}
}
