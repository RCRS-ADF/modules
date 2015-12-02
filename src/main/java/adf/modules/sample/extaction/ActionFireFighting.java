package adf.modules.sample.extaction;

import adf.agent.action.common.ActionMove;
import adf.agent.action.common.ActionRest;
import adf.agent.action.fire.ActionExtinguish;
import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.PathPlanning;
import adf.component.extaction.ExtAction;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;
import java.util.List;

public class ActionFireFighting extends ExtAction {

    private WorldInfo worldInfo;
    private AgentInfo agentInfo;
    private PathPlanning pathPlanning;
    private int maxDistance;
    private int maxPower;
    private EntityID target;

    public ActionFireFighting(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PathPlanning pathPlanning, EntityID target) {
        super();
        this.worldInfo = worldInfo;
        this.agentInfo = agentInfo;
        this.pathPlanning = pathPlanning;
        this.target = target;
        this.maxDistance = scenarioInfo.getFireExtinguishMaxDistance();
        this.maxPower = scenarioInfo.getFireExtinguishMaxSum();
    }

    @Override
    public ExtAction calc() {
        this.result = new ActionRest();
        if (worldInfo.getDistance(agentInfo.getID(), this.target) <= maxDistance) {
            this.result = new ActionExtinguish(this.target, maxPower);
        }
        else {
            List<EntityID> path = planPathToFire(this.target);
            if (path != null) {
                this.result = new ActionMove(path);
            }
        }
        return this;
    }

    private List<EntityID> planPathToFire(EntityID target) {
        // Try to get to anything within maxDistance of the target
        Collection<EntityID> targets = this.worldInfo.getObjectIDsInRange(target, maxDistance);
        if (targets.isEmpty()) {
            return null;
        }
        this.pathPlanning.setFrom(this.agentInfo.getPosition());
        this.pathPlanning.setDestination(targets);
        return this.pathPlanning.getResult();
    }
}
