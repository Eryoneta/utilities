package architecture.state_flow.state;

public enum StateUnitIndex {
	UNIT_1(1),		//1
	UNIT_2(2),		//2
	UNIT_3(3),		//3
	UNIT_4(4),		//4
	UNIT_5(5),		//5
	UNIT_6(6),		//6
	UNIT_7(7),		//7
	UNIT_8(8),		//8
	UNIT_9(9),		//9
	UNIT_10(10),	//10
	UNIT_11(11),	//11
	UNIT_12(12),	//12
	UNIT_13(13),	//13
	UNIT_14(14),	//14
	UNIT_15(15),	//15
	UNIT_16(16),	//16
	UNIT_17(17),	//17
	UNIT_18(18),	//18
	UNIT_19(19),	//19
	UNIT_20(20),	//20
	UNIT_21(21),	//21
	UNIT_22(22),	//22
	UNIT_23(23),	//23
	UNIT_24(24);	//24
	// VALUE
	private int value;
	public int getValue() {
		return value;
	}
	// MAIN
	StateUnitIndex(int value) {
		this.value = value;
	}
}