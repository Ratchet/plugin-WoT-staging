/* This code is part of WoT, a plugin for Freenet. It is distributed 
 * under the GNU General Public License, version 2 (or at your option
 * any later version). See http://www.gnu.org/ for details of the GPL. */
package plugins.WebOfTrust.ui.web;

import plugins.WebOfTrust.introduction.IntroductionPuzzleStore;
import freenet.clients.http.ToadletContext;
import freenet.l10n.BaseL10n;
import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;

/**
 * The HomePage of the plugin.
 * 
 * @author xor (xor@freenetproject.org)
 * @author Julien Cornuwel (batosai@freenetproject.org)
 */
public class HomePage extends WebPageImpl {

	/**
	 * Creates a new HomePage.
	 * 
	 * @param toadlet A reference to the {@link WebInterfaceToadlet} which created the page, used to get resources the page needs.
	 * @param myRequest The request sent by the user.
	 */
	public HomePage(WebInterfaceToadlet toadlet, HTTPRequest myRequest, ToadletContext context, BaseL10n _baseL10n) {
		super(toadlet, myRequest, context, _baseL10n);
	}

	public void make() {
		makeSummary();
	}

	/**
	 * Creates a short summary of what the plugin knows of the WoT.
	 */
	private void makeSummary() {
		HTMLNode box = addContentBox(l10n().getString("HomePage.SummaryBox.Header"));
		
		synchronized(wot) {
		HTMLNode list = new HTMLNode("ul");
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.OwnIdentities") + ": " + wot.getAllOwnIdentities().size()));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.KnownIdentities") + ": " + wot.getAllNonOwnIdentities().size()));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.TrustRelationships") + ": " + wot.getAllTrusts().size()));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.ScoreRelationships") + ": " + wot.getAllScores().size()));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.FullRecomputations") + ": " + wot.getNumberOfFullScoreRecomputations()));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.FullRecomputationTime") + ": " + wot.getAverageFullScoreRecomputationTime()));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.IncrementalRecomputations") + ": " + wot.getNumberOfIncrementalScoreRecomputations()));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.IncrementalRecomputationTime") + ": " + wot.getAverageIncrementalScoreRecomputationTime()));
		IntroductionPuzzleStore puzzleStore = wot.getIntroductionPuzzleStore();
		synchronized(puzzleStore) {
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.UnsolvedOwnCaptchas") + ": " + puzzleStore.getOwnCatpchaAmount(false)));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.SolvedOwnCaptchas") + ": " + puzzleStore.getOwnCatpchaAmount(true)));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.UnsolvedCaptchasOfOthers") + ": " + puzzleStore.getNonOwnCaptchaAmount(false)));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.SolvedCaptchasOfOthers") + ": " + puzzleStore.getNonOwnCaptchaAmount(true)));
		list.addChild(new HTMLNode("li", l10n().getString("HomePage.SummaryBox.NotInsertedCaptchasSolutions") + ": " + puzzleStore.getUninsertedSolvedPuzzles().size()));
		}
		box.addChild(list);
		}
	}
}
