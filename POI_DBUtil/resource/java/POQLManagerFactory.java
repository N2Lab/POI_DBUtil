

import java.util.ResourceBundle;

import jp.co.powerbeans.powerql.POQLManager;

/**
 * PowerQLインスタンスを返す
 * @author Akihiko Monden
 *
 */
public final class POQLManagerFactory {

    private POQLManagerFactory() {}

    private static final POQLManager manager;
    
   /**
    * POQLManager を生成(ユーザー,パスワード指定)
    */
    static {
         POQLManager lmanager = null;
         try {
             lmanager = new POQLManager(
            		 getResource("driver"),
            		 getResource("url"),
            		 getResource("user"), 
            		 getResource("password"), 
            		 getResource("sequence.tbl"));
         } catch (Exception e) {
             e.printStackTrace();
         }
         manager = lmanager;
    }

    /**
     * 唯一のmanagerインスタンスを返す
     * @return Returns the manager.
     */
    public static POQLManager getManager() {
        return manager;
    }

	/**
	 * @param key
	 * @return
	 */
	private static String getResource(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("poql");
		return bundle.getString(key);
	}

}
