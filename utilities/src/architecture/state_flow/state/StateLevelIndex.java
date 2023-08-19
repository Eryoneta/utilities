package architecture.state_flow.state;

public enum StateLevelIndex {
	LEVEL_1(0),		//1 - 24
	LEVEL_2(1),		//25 - 600
	LEVEL_3(2),		//625 - 15.000
	LEVEL_4(3),		//15.625 - 375.000
	LEVEL_5(4),		//390.625 - 9.375.000
	LEVEL_6(5),		//9.765.625 - 234.375.000
	LEVEL_7(6),		//244.140.625 - 5.859.375.000
	LEVEL_8(7),		//6.103.515.625 - 146.484.375.000
	LEVEL_9(8),		//152.587.890.625 - 3.662.109.375.000
	LEVEL_10(9),	//3.814.697.265.625 - 91.552.734.375.000
	LEVEL_11(10),	//95.367.431.640.625 - 2.288.818.359.375.000
	LEVEL_12(11),	//2.384.185.791.015.625 - 57.220.458.984.375.000
	LEVEL_13(12);	//59.604.644.775.390.625 - 1.430.511.474.609.375.000
	// ACIMA DO LIMITE DE LONG! (9.223.372.036.854.775.807)
	// OU ATÉ DE UNSIGNED_LONG! (18.446.744.073.709.551.615)
//	LEVEL_14(13);	//1.490.116.119.384.765.625 - 35.762.786.865.234.375.000
	// VALUE
	private int value;
	public int getValue() {
		return value;
	}
	// MAIN
	StateLevelIndex(int value) {
		this.value = value;
	}
}