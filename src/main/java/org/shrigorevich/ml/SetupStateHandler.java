package org.shrigorevich.ml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shrigorevich.ml.domain.mobs.MobService;
import org.shrigorevich.ml.domain.npc.NpcService;
import org.shrigorevich.ml.domain.project.BuildProject;
import org.shrigorevich.ml.domain.project.ProjectService;
import org.shrigorevich.ml.domain.scoreboard.ScoreboardService;
import org.shrigorevich.ml.domain.structures.Structure;
import org.shrigorevich.ml.domain.structures.StructureService;
import org.shrigorevich.ml.domain.structures.TownInfra;

import java.util.List;

public class SetupStateHandler implements Listener {

    private final Logger logger;
    private final StructureService structureService;
    private final ProjectService projectService;
    private final ScoreboardService scoreboardService;
    private final NpcService npcService;
    private final MobService mobService;

    public SetupStateHandler(
            StructureService structureService,
            ProjectService projectService,
            NpcService npcService,
            MobService mobService,
            ScoreboardService boardService) {
        this.structureService = structureService;
        this.projectService = projectService;
        this.npcService = npcService;
        this.mobService = mobService;
        this.scoreboardService = boardService;
        this.logger = LogManager.getLogger("SetupStateHandler");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void SetupState(SetupStateEvent event) {
        try {
            structureService.setup();
            projectService.setup();
            setupProjects();
            npcService.setup();
            mobService.setup();
            logger.info("All services are loaded successfully");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    private void setupProjects() {
        List<Structure> structs = structureService.getDamagedStructs();
        for (Structure s : structs) {
            if (s instanceof TownInfra ti) {
                BuildProject project = projectService.createProject((TownInfra) s, ti.getStructBlocks().size());
                project.addPlannedBlocks(ti.getBrokenBlocks());
                projectService.addProject(project);
                logger.debug(String.format("Project loaded: " + project.getId()));
            }
        }
        projectService.getCurrent().ifPresent(project -> {
            logger.debug(String.format("Current project: %s. Broken blocks: %d%n", project.getStruct().getName(), project.getBrokenSize()));
            scoreboardService.updateScoreboard(project, projectService.getResources());
        });
    }
}
