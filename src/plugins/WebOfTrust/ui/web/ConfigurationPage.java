/* This code is part of WoT, a plugin for Freenet. It is distributed 
 * under the GNU General Public License, version 2 (or at your option
 * any later version). See http://www.gnu.org/ for details of the GPL. */
package plugins.WebOfTrust.ui.web;

import java.util.Arrays;

import plugins.WebOfTrust.Configuration;
import plugins.WebOfTrust.IdentityFetcher;
import freenet.clients.http.ToadletContext;
import freenet.l10n.BaseL10n;
import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;

/**
 * @author xor (xor@freenetproject.org)
 */
public class ConfigurationPage extends WebPageImpl {

	/**
	 * @param myRequest The request sent by the user.
	 * @param _baseL10n l10n handle
	 * @param toadlet A reference to the {@link WebInterfaceToadlet} which created the page, used to get resources the page needs.
	 */
	
	IdentityFetcher mFetcher;
	
	public ConfigurationPage(WebInterfaceToadlet toadlet, HTTPRequest myRequest, ToadletContext context, BaseL10n _baseL10n, IdentityFetcher mFetcher) {
		super(toadlet, myRequest, context, _baseL10n);
		this.mFetcher = mFetcher;
	}

	// TODO: Maybe use or steal freenet.clients.http.ConfigToadlet
	public void make() {
		HTMLNode list1 = new HTMLNode("ul");
		HTMLNode list2 = new HTMLNode("ul");
		
		Configuration config = wot.getConfig();
		/*TODO remove this check when standby is default config variable*/
		if(!config.containsInt("standby")) {
			config.set("standby", 0);
			config.storeAndCommit();
		}		

		if(request.isPartSet("ToggleStandby")) {
			Integer newStandby;
		
			if(config.getInt("standby")==0) {
				newStandby = 1;
				synchronized(config) {
					config.set("standby", newStandby); //do this before mFetcher.startStandbyMode() now, in case that keeps hanging
					config.storeAndCommit();
				}
				mFetcher.startStandbyMode();
			} else {
				newStandby = 0;
				synchronized(config) {
					config.set("standby", newStandby); //do this before mFetcher.stopStandbyMode() now, in case that keeps hanging
					config.storeAndCommit();
				}
				mFetcher.stopStandbyMode();
			}
		}
		synchronized(config) {
			String[] intKeys = config.getAllIntKeys();
			String[] stringKeys = config.getAllStringKeys();
			
			Arrays.sort(intKeys);
			Arrays.sort(stringKeys);

			for(String key : intKeys) list1.addChild(new HTMLNode("li", key + ": " + config.getInt(key)));
			for(String key : stringKeys) list1.addChild(new HTMLNode("li", key + ": " + config.getString(key)));
		}

		HTMLNode box = addContentBox(l10n().getString("ConfigurationPage.ConfigurationBox.Header"));
		box.addChild(list1);
		box.addChild(list2);
		
		HTMLNode toggleStandbyForm = pr.addFormChild(box, uri, "ToggleStandby");
		toggleStandbyForm.addChild("input", new String[] { "type", "name", "value" }, new String[] { "hidden", "page", "ToggleStandby" });
		toggleStandbyForm.addChild("input", new String[] { "type", "name", "value" }, new String[] { "submit", "ToggleStandby", l10n().getString("ConfigurationPage.ConfigurationBox.ToggleStandbyButton") });
		toggleStandbyForm.addChild("b",l10n().getString("ConfigurationPage.ConfigurationBox.ToggleStandbyWarning"));
	}
}
