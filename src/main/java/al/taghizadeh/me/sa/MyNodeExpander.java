package al.taghizadeh.me.sa;

import al.taghizadeh.csp.Assignment;
import al.taghizadeh.framework.Node;
import al.taghizadeh.framework.NodeExpander;
import al.taghizadeh.framework.problem.Problem;
import al.taghizadeh.util.Util;

import java.util.List;

/**
 * Created by Ali Asghar on 06/07/2017.
 */
public class MyNodeExpander<S extends Assignment, A extends SwapTimeSlotAction> extends NodeExpander<S, A> {

    @Override
    public Node<S, A> createRootNode(S state) {
        return super.createRootNode(state);
    }

    @Override
    public NodeExpander useParentLinks(boolean s) {
        return super.useParentLinks(false);
    }

    @Override
    public Node<S, A> createNode(S state, Node<S, A> parent, A action, double stepCost) {
        return super.createNode(state, parent, action, stepCost);
    }

    @Override
    public List<Node<S, A>> expand(Node<S, A> node, Problem<S, A> problem) {
        return super.expand(node, problem);
    }

    public List<A> expandActions(Node<S, A> node, Problem<S, A> problem){
        return problem.getActions(node.getState());
    }

    public Node<S, A> selectRandomChild(Node<S, A> current, Problem<S, A> p) {
        A action = Util.selectRandomlyFromList(expandActions(current, p));
        return new Node<>(p.getResult(current.getState(), action));
    }

}
