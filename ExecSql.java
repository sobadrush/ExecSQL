/**
 * 執行 SQL 工具
 */
public class ExecSql {

	public static void main(String[] args) throws IOException {
		new ExecSql(Deploy.STG).exec();
	}

	private PlinkCMD plinkCMD;

	/**
	 * @param deploy 要執行的環境
	 * @throws IOException
	 */
	public ExecSql(Deploy deploy) throws IOException {
		this.plinkCMD = new PlinkCMD(deploy);
	}

	/**
	 * 預設的執行方法
	 * 預設 sql 檔放在本專案的 resources/exec.sql
	 *
	 * @throws IOException
	 */
	public void exec() throws IOException {
		File sql = new ClassPathResource("exec.sql").getFile();
		exec(sql);
	}

	/**
	 * 執行 sql 方法
	 *
	 * @param sql 欲執行的 sql 檔案
	 * @throws IOException
	 */
	public void exec(File sql) throws IOException {
		File script = new ClassPathResource("execsql_script").getFile();
		String tempScript = FileUtils.readFileToString(script)
			.replace("{{SQL}}", FileUtils.readFileToString(sql))
			.replace("{{UUID}}", UUID.randomUUID().toString());
		File tempFile = new File("./temp_script");
		FileUtils.write(tempFile, tempScript);
		plinkCMD.execCMD(tempFile);
	}

}