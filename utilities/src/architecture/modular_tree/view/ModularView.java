package architecture.modular_tree.view;

import java.util.ArrayList;
import java.util.List;

import architecture.modular_tree.root.ModularRoot;
import architecture.rrf_vp.view.ViewJoint;

public abstract class ModularView<
	R extends ModularRoot<R, ?, ?, J, ?>,
	J extends ModularView<R, J, VL>,
	VL extends ModularViewListener<J, VL>
> implements ViewJoint<R, J> {
	// LISTENERS
	private List<VL> viewListeners = new ArrayList<>();
	public List<VL> getViewListeners() {
		return viewListeners;
	}
	public void addViewListener(VL viewListener) {
		viewListeners.add(viewListener);
	}
}