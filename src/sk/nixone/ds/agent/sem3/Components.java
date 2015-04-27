package sk.nixone.ds.agent.sem3;

import OSPABA.IdList;

public class Components extends IdList {
	public static final int A_MODEL = 1;
	public static final int A_VEHICLE_MOVEMENT = 2;
	public static final int A_SURROUNDING = 3;
	public static final int A_BOARDING = 4;
	public static final int A_EXITING = 5;
	
	public static final int M_MODEL = 101;
	public static final int M_VEHICLE_MOVEMENT = 102;
	public static final int M_SURROUNDING = 103;
	public static final int M_BOARDING = 104;
	public static final int M_EXITING = 105;
	
	public static final int ARRIVAL_PLANNER = 201;
	public static final int MOVEMENT_PLANNER = 202;
	public static final int BOARDING_PLANNER = 203;
	public static final int VEHICLE_INIT_PLANNER = 204;
	public static final int EXITING_PLANNER = 205;
}
