public class PlinkCMD {

	private String address;
	private String username;
	private String pwd;
	private File plink;

	private String cmdCharset; // CMD 輸出的編碼，WIN 中為 "Big5"

	public PlinkCMD(Deploy deploy) throws IOException {
		this(deploy.getAddress(), deploy.getUsername(), deploy.getPwd(), "UTF-8");
	}

	public PlinkCMD(Deploy deploy, String cmdCharset) throws IOException {
		this(deploy.getAddress(), deploy.getUsername(), deploy.getPwd(), cmdCharset);
	}

	public PlinkCMD(String address, String username, String pwd, String cmdCharset) throws IOException {
		this.address = address;
		this.username = username;
		this.pwd = pwd;
		this.plink = new ClassPathResource("PLINK.EXE").getFile();
		this.cmdCharset = cmdCharset;
	}

	public PlinkCMD(String address, String username, String pwd, String cmdCharset, String plinkPath) {
		this.address = address;
		this.username = username;
		this.pwd = pwd;
		this.plink = new File(plinkPath);
		this.cmdCharset = cmdCharset;
	}

	private String getCMD(String cmd) {
		return String.format("%s %s -l %s -pw %s %s", plink.getPath(), address, username, pwd, cmd);
	}

	public void execCMD(File file) throws IOException {
		execCMD(String.format("-m %s", file.getAbsolutePath()), null);
	}

	public void execCMD(String command) throws IOException {
		execCMD(String.format("\"%s\"", command), null);
	}

	public void execCMD(String command, BiConsumer<Process, String> afterRun) throws IOException {
		String cmd = getCMD(command);
		System.out.printf("執行指令 -> %s%n", cmd);
		Process powerShellProcess = Runtime.getRuntime().exec(cmd);
		powerShellProcess.getOutputStream().close(); // Getting the results
		String line;
		System.out.println("Standard Output:");
		try (BufferedReader stdout = new BufferedReader(
			new InputStreamReader(powerShellProcess.getInputStream(), cmdCharset));) {
			while ((line = stdout.readLine()) != null) {
				String text = new String(line.getBytes(cmdCharset), cmdCharset);
				System.out.println(text);
				if (afterRun != null) {
					afterRun.accept(powerShellProcess, text);
				}
			}
		}
		System.out.println("Standard Error:");
		try (BufferedReader stderr = new BufferedReader(
			new InputStreamReader(powerShellProcess.getErrorStream(), cmdCharset));) {
			while ((line = stderr.readLine()) != null) {
				System.out.println(new String(line.getBytes(cmdCharset), cmdCharset));
			}
		}
		System.out.println("Done");
		powerShellProcess.destroy();
	}

}