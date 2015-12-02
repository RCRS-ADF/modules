package adf.modules.sample.extaction;

import adf.agent.action.common.ActionMove;
import adf.agent.action.common.ActionRest;
import adf.agent.info.AgentInfo;
import adf.component.algorithm.PathPlanning;
import adf.component.complex.TargetSelector;
import adf.component.extaction.ExtAction;
import rescuecore2.standard.entities.Building;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class ActionSearchCivilian extends ExtAction {
    private AgentInfo agentInfo;

    private PathPlanning pathPlanning;

    private TargetSelector<Building> buildingSelector;

    public ActionSearchCivilian(AgentInfo agentInfo, PathPlanning pathPlanning, TargetSelector<Building> buildingSelector) {
        super();
        this.agentInfo = agentInfo;
        this.pathPlanning = pathPlanning;
        this.buildingSelector = buildingSelector;
    }

    @Override
    public ExtAction calc() {
        this.result = new ActionRest();
        EntityID searchBuildingID = this.buildingSelector.calc().getTarget();
        if(searchBuildingID != null) {
            this.pathPlanning.setFrom(agentInfo.getPosition());
            List<EntityID> path = this.pathPlanning.setDestination(searchBuildingID).getResult();
            if (path != null) {
                path.remove(path.size() - 1);
                this.result = new ActionMove(path);
            }
        }
        return this;
    }
}
