package actions;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;

/**
 * Intercepta todos los "actions" (metodos de controllers) 
 * metiendo un delay de 1.5 segundos.
 * 
 * Obviamente esto no es muy util en una aplicacion real.
 * Lo uso para simular que tarde un ratito el server en contestar,
 * así en el cliente se ve el asincronismo, y además, el feedback de
 * la "ruedita" cargando de la libreria "loading-bar".
 * 
 * Se usa anotando el controller o cada método con @Delayed
 * 
 * @author jfernandes
 */
public class DelayAction extends Action<Delayed> {

	@Override
	public Promise call(Context context) throws Throwable {
		Thread.sleep(1500);
		return delegate.call(context);
	}

}
