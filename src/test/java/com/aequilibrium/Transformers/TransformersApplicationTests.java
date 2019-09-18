package com.aequilibrium.Transformers;

import com.aequilibrium.Transformers.controllers.TransformerController;
import com.aequilibrium.Transformers.dynamo.TransformersTable;
import com.aequilibrium.Transformers.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransformersApplicationTests {
	TransformerController controller = new TransformerController();
	TransformersTable table = new TransformersTable();
	private static final String AUTOBOT_WIN = "Autobot Win!";
	private static final String DECEPTICON_WIN = "Decepticon Win!";
	private static final String TIE = "Tie";
	private static final String TOO_MUCH_POWER = "Power Overload!!!";
	private static final String OPTIMUS_PRIME = "Optimus Prime";
	private static final String PREDAKING = "Predaking";

	@Test
	public void contextLoads() {
	}

	@Test
	public void successfullyCreateTransformer() {
		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(5);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> response = controller.createTransformer(request);

		assert(response.getStatusCode().equals(HttpStatus.OK));

		Transformer transformer = response.getBody();

		assert(transformer != null);

		// clean up
		table.delete(transformer);
	}

	@Test
	public void successfullyCreateTransformerLowercaseAllegiance() {
		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Predaking");
		request.setStrength(10);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(5);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('d');

		ResponseEntity<Transformer> response = controller.createTransformer(request);

		assert(response.getStatusCode().equals(HttpStatus.OK));

		Transformer transformer = response.getBody();

		assert(transformer != null);

		// clean up
		table.delete(transformer);
	}

	@Test
	public void createTransformerNoName() {
		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(5);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		try {
			ResponseEntity<Transformer> response = controller.createTransformer(request);
			table.delete(response.getBody());
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("name"));
		}
	}

	@Test
	public void createTransformerInvalidAllegiance() {
		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(5);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('W');

		try {
			ResponseEntity<Transformer> response = controller.createTransformer(request);
			table.delete(response.getBody());
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("allegiance"));
		}
	}

	@Test
	public void createTransformerHighStats() {
		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(11);
		request.setIntelligence(5);
		request.setSpeed(5);
		request.setEndurance(5);
		request.setRank(5);
		request.setCourage(5);
		request.setFirepower(5);
		request.setSkill(5);
		request.setAllegiance('A');

		try {
			ResponseEntity<Transformer> response = controller.createTransformer(request);
			table.delete(response.getBody());
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("strength"));
		}
	}

	@Test
	public void createTransformerLowStats() {
		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(5);
		request.setIntelligence(5);
		request.setSpeed(5);
		request.setEndurance(5);
		request.setRank(0);
		request.setCourage(5);
		request.setFirepower(5);
		request.setSkill(5);
		request.setAllegiance('A');

		try {
			ResponseEntity<Transformer> response = controller.createTransformer(request);
			table.delete(response.getBody());
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("rank"));
		}
	}

	@Test
	public void listTransformers() {
		ResponseEntity<List<Transformer>> response = controller.listTransformers();
		int previousListSize = response.getBody().size();

		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(5);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> transformerResponse = controller.createTransformer(request);
		Transformer transformer = transformerResponse.getBody();

		response = controller.listTransformers();
		assert(response.getBody().size() == previousListSize + 1);

		table.delete(transformer);

		response = controller.listTransformers();
		assert(response.getBody().size() == previousListSize);
	}

	@Test
	public void successfullyDeleteTransformer() {
		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(5);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> response = controller.createTransformer(request);
		Transformer transformer = response.getBody();

		assert(transformer != null);

		transformer = table.getById(transformer.getId());

		assert(transformer != null);

		response = controller.deleteTransformerById(transformer.getId());

		transformer = response.getBody();

		assert(transformer != null);

		transformer = table.getById(transformer.getId());

		assert(transformer == null);
	}

	@Test
	public void deleteTransformerNoID() {
		try {
			controller.deleteTransformerById("");
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("required"));
		}
	}

	@Test
	public void deleteTransformerInvalidId() {
		try {
			controller.deleteTransformerById("000000000000000000");
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("exist"));
		}
	}

	@Test
	public void successfullyUpdateTransformer() {
		CreateTransformerRequest createRequest = new CreateTransformerRequest();
		createRequest.setName("Optimus Prime");
		createRequest.setStrength(1);
		createRequest.setIntelligence(2);
		createRequest.setSpeed(3);
		createRequest.setEndurance(4);
		createRequest.setRank(5);
		createRequest.setCourage(6);
		createRequest.setFirepower(7);
		createRequest.setSkill(8);
		createRequest.setAllegiance('A');

		ResponseEntity<Transformer> createResponse = controller.createTransformer(createRequest);
		Transformer transformer = createResponse.getBody();

		UpdateTransformerRequest request = new UpdateTransformerRequest();
		request.setId(transformer.getId());
		request.setName("Predaking");
		request.setStrength(10);
		request.setAllegiance('D');
		controller.updateTransformer(request);

		transformer = table.getById(transformer.getId());

		assert(transformer.getName().equals("Predaking"));
		assert(transformer.getStrength() == 10);
		assert(transformer.getAllegiance() == 'D');
	}

	@Test
	public void updateTransfomerNoId() {
		UpdateTransformerRequest request = new UpdateTransformerRequest();
		request.setName("Predaking");
		request.setStrength(10);
		request.setAllegiance('D');

		try {
			controller.updateTransformer(request);
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("required"));
		}
	}

	@Test
	public void updateTransformerEmptyName() {
		UpdateTransformerRequest request = new UpdateTransformerRequest();
		request.setId("0000000000000");
		request.setName("");
		request.setStrength(10);
		request.setAllegiance('D');

		try {
			controller.updateTransformer(request);
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("name"));
		}
	}

	@Test
	public void updateTransformerInvalidStat() {
		UpdateTransformerRequest request = new UpdateTransformerRequest();
		request.setId("0000000000000");
		request.setName("Predaking");
		request.setStrength(99);
		request.setAllegiance('D');

		try {
			controller.updateTransformer(request);
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("strength"));
		}
	}

	@Test
	public void updateTransformerInvalidAllegiance() {
		UpdateTransformerRequest request = new UpdateTransformerRequest();
		request.setId("0000000000000");
		request.setName("Predaking");
		request.setStrength(10);
		request.setAllegiance('W');

		try {
			controller.updateTransformer(request);
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("allegiance"));
		}
	}

	@Test
	public void tooMuchPowerBattle() {
		List<String> transormerIds = new LinkedList<>();

		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(10);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Predaking");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Bumblebee");
		request.setAllegiance('A');
		request.setRank(1);

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Starscream");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		BattleRequest battleRequest = new BattleRequest();

		battleRequest.setTransformerIds(transormerIds);

		ResponseEntity<BattleResponse> battleResponse = controller.battle(battleRequest);

		// cleanup
		for (String id : transormerIds) {
			table.delete(table.getById(id));
		}

		assert(battleResponse.getStatusCode() == HttpStatus.OK);
		assert(battleResponse.getBody().getNumBattles() == 1);
		assert(battleResponse.getBody().getWinningTeam() == "There was no survivors...");
		assert(battleResponse.getBody().getSurvivingAutobots().size() == 0);
		assert(battleResponse.getBody().getSurvivingDecepticons().size() == 0);
	}

	@Test
	public void autobotWinBattle() {
		List<String> transormerIds = new LinkedList<>();

		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(10);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Logic");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Bumblebee");
		request.setAllegiance('A');
		request.setRank(1);

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Starscream");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		BattleRequest battleRequest = new BattleRequest();

		battleRequest.setTransformerIds(transormerIds);

		ResponseEntity<BattleResponse> battleResponse = controller.battle(battleRequest);

		// cleanup
		for (String id : transormerIds) {
			table.delete(table.getById(id));
		}

		assert(battleResponse.getStatusCode() == HttpStatus.OK);
		assert(battleResponse.getBody().getNumBattles() == 2);
		assert(battleResponse.getBody().getWinningTeam().toLowerCase().contains("autobots"));
		assert(battleResponse.getBody().getSurvivingAutobots().size() == 1);
		assert(battleResponse.getBody().getSurvivingDecepticons().size() == 0);
	}

	@Test
	public void decepticonWinBattle() {
		List<String> transormerIds = new LinkedList<>();

		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Megan Fox");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(10);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Predaking");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Bumblebee");
		request.setAllegiance('A');
		request.setRank(1);

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Starscream");
		request.setAllegiance('D');
		request.setIntelligence(10);

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		BattleRequest battleRequest = new BattleRequest();

		battleRequest.setTransformerIds(transormerIds);

		ResponseEntity<BattleResponse> battleResponse = controller.battle(battleRequest);

		// cleanup
		for (String id : transormerIds) {
			table.delete(table.getById(id));
		}

		assert(battleResponse.getStatusCode() == HttpStatus.OK);
		assert(battleResponse.getBody().getNumBattles() == 2);
		assert(battleResponse.getBody().getWinningTeam().toLowerCase().contains("decepticon"));
		assert(battleResponse.getBody().getSurvivingAutobots().size() == 0);
		assert(battleResponse.getBody().getSurvivingDecepticons().size() == 2);
	}

	@Test
	public void tieBattle() {
		List<String> transormerIds = new LinkedList<>();

		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Megan Fox");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(10);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Shia");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Bumblebee");
		request.setAllegiance('A');
		request.setRank(1);

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Starscream");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		BattleRequest battleRequest = new BattleRequest();

		battleRequest.setTransformerIds(transormerIds);

		ResponseEntity<BattleResponse> battleResponse = controller.battle(battleRequest);

		// cleanup
		for (String id : transormerIds) {
			table.delete(table.getById(id));
		}

		assert(battleResponse.getStatusCode() == HttpStatus.OK);
		assert(battleResponse.getBody().getNumBattles() == 2);
		assert(battleResponse.getBody().getWinningTeam().toLowerCase().contains("tie"));
		assert(battleResponse.getBody().getSurvivingAutobots().size() == 0);
		assert(battleResponse.getBody().getSurvivingDecepticons().size() == 0);
	}

	@Test
	public void noAutobotsBattle() {
		List<String> transormerIds = new LinkedList<>();

		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(10);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('D');

		ResponseEntity<Transformer> response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Predaking");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Bumblebee");
		request.setAllegiance('D');
		request.setRank(1);

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Starscream");
		request.setAllegiance('D');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		BattleRequest battleRequest = new BattleRequest();

		battleRequest.setTransformerIds(transormerIds);

		try {
			controller.battle(battleRequest);
			// cleanup
			for (String id : transormerIds) {
				table.delete(table.getById(id));
			}
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("autobot"));
		}


		// cleanup
		for (String id : transormerIds) {
			table.delete(table.getById(id));
		}
	}

	@Test
	public void noDecepticonsBattle() {
		List<String> transormerIds = new LinkedList<>();

		CreateTransformerRequest request = new CreateTransformerRequest();
		request.setName("Optimus Prime");
		request.setStrength(1);
		request.setIntelligence(2);
		request.setSpeed(3);
		request.setEndurance(4);
		request.setRank(10);
		request.setCourage(6);
		request.setFirepower(7);
		request.setSkill(8);
		request.setAllegiance('A');

		ResponseEntity<Transformer> response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Predaking");
		request.setAllegiance('A');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Bumblebee");
		request.setAllegiance('A');
		request.setRank(1);

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		request.setName("Starscream");
		request.setAllegiance('A');

		response = controller.createTransformer(request);
		transormerIds.add(response.getBody().getId());

		BattleRequest battleRequest = new BattleRequest();

		battleRequest.setTransformerIds(transormerIds);

		try {
			controller.battle(battleRequest);
			// cleanup
			for (String id : transormerIds) {
				table.delete(table.getById(id));
			}
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("decepticon"));
		}

		// cleanup
		for (String id : transormerIds) {
			table.delete(table.getById(id));
		}
	}

	@Test
	public void noTransformersBattle() {
		BattleRequest battleRequest = new BattleRequest();

		try {
			controller.battle(battleRequest);
			fail();
		} catch (ResponseStatusException e) {
			assert(e.getMessage().toLowerCase().contains("2 transformers"));
		}
	}
}
