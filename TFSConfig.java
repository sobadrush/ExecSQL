public enum TFSConfig {

	UT(Stage.UT, new HashMap<String, String>() {{
		put("pipelines", "ibm-common-component-UT,channel-component-UT");
	}}),
	STG(Stage.STG, new HashMap<String, String>() {{
		put("pipelines", "ibm-common-component-STG,channel-component-STG");
	}}),
	UNKNOWN,
	;

	private Stage stage;
	private HashMap<String, String> params = new HashMap<>();

	TFSConfig() {
	}

	TFSConfig(Stage stage, HashMap<String, String> params) {
		this.stage = stage;
		this.params = params;
	}

	public static TFSConfig findByStage(Stage stage) {
		Optional<TFSConfig> opTFSConfig = Arrays.stream(values())
			.filter(config -> stage.equals(config.getStage()))
			.findFirst();
		return opTFSConfig.orElse(UNKNOWN);
	}

	public String[] getPipelines() {
		return params.getOrDefault("pipelines", "").split(",");
	}

	public Stage getStage() {
		return stage;
	}

	public HashMap<String, String> getParams() {
		return params;
	}
}