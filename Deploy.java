public enum Deploy {

	UT(Stage.UT, "DEV", "88.8.111.66", "dmmbadm", "dmmbadm"),
	UT2(Stage.UT, "UT-88.249.29.19", "88.249.29.19", "dmmbadm", "dmmbadm"),
	STG(Stage.STG, "UAT-Node1", "88.8.195.76", "tmmbadm", "tmmbadm"),
	STG2(Stage.STG, "UAT-Node3", "88.8.195.57", "tmmbadm", "tmmbadm"),
	UAT(Stage.UAT, "UAT-Node2", "88.8.195.77", "tmmbadm", "tmmbadm"),
	UAT2(Stage.UAT, "UAT-Node4", "88.8.195.58", "tmmbadm", "tmmbadm"),
	UNKNOWN,
	;

	private Stage stage;
	private String tfsDeployName;
	private String address;
	private String username;
	private String pwd;

	Deploy() {
	}

	Deploy(Stage stage, String tfsDeployName, String address, String username, String pwd) {
		this.stage = stage;
		this.tfsDeployName = tfsDeployName;
		this.address = address;
		this.username = username;
		this.pwd = pwd;
	}

	public static Deploy findDeploy(String branch, String tfsDeployName) {
		for (Deploy config : Deploy.values()) {
			if (StringUtils.containsIgnoreCase(branch, config.getStage().getBranchKeyword())
				&& StringUtils.containsIgnoreCase(tfsDeployName, config.getTfsDeployName())) {
				return config;
			}
		}
		return UNKNOWN;
	}

	public static List<Deploy> findDeploysByStage(Stage stage) {
		return Arrays.stream(values())
			.filter(deploy -> stage.equals(deploy.stage))
			.collect(Collectors.toList());
	}

	public Stage getStage() {
		return stage;
	}

	public String getTfsDeployName() {
		return tfsDeployName;
	}

	public String getAddress() {
		return address;
	}

	public String getUsername() {
		return username;
	}

	public String getPwd() {
		return pwd;
	}

}