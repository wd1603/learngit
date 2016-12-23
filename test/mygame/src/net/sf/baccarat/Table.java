package net.sf.baccarat;

import java.util.ArrayList;
import java.util.List;

abstract class Table {
	List<Actor> actors = new ArrayList<Actor>();
	
	abstract void enter(Actor actor);
	
	abstract void sit(Actor actor);
	
	abstract void bet(Actor actor);
}

//class InitState extends Table {
//
//	@Override
//	void enter(Actor actor) {
//		
//	}
//
//	@Override
//	void sit(Actor actor) {
//		
//	}
//	
//}
//
//class BetState extends Table {
//
//	@Override
//	void enter(Actor actor) {
//		
//	}
//	
//}
//
//class DealCardState extends Table {
//
//	@Override
//	void enter(Actor actor) {
//		
//	}
//	
//}
//
//class BalanceState extends Table {
//
//	@Override
//	void enter(Actor actor) {
//		
//	}
//	
//}