package a2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements ActionListener, Observer {

	private Model model;
	private View view;
	// Timer f�r die automatische Zugausf�hrung
	private Timer timer;
	
	public Controller(Model model, View view){
		this.model = model;
		this.view = view;
		
		// Model beobachten
		model.addObserver(this);
		
		// Buttons der View beobachten
		view.getBtnNextTurn().addActionListener(this);
		view.getBtnSchnellZiehenAn().addActionListener(this);
		view.getBtnSchnellZiehenAus().addActionListener(this);
		
		// View das erste Mal die Daten des Models �bergeben
		view.updateView(Model.getWorld(), Model.getAmeisen());
		
		// View anzeigen
		view.setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 == model){
			// Wenn das Model seinen Zug beendet hat, wird die View aktualisiert
			view.updateView(Model.getWorld(), Model.getAmeisen());
			view.getLblFutterGesammelt().setText(new Integer(Model.getFutterGesammelt()).toString());
			view.getLblZgeInsgesamt().setText(new Integer(Model.getZgeInsg()).toString());
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent act) {
		// Je nach Knopf, der gedr�ckt wurde, wird ein anderer ActionCommand gesendet
		switch (act.getActionCommand()){
			// Einzelnen Zug ausf�hren
			case View.ACT_TURN:
				model.zugAusf�hren();
				break;
				
			// Automatische Zugausf�hrung starten
			case View.ACT_AUTO_ON:
				view.getBtnSchnellZiehenAn().setEnabled(false);
				view.getBtnSchnellZiehenAus().setEnabled(true);
				view.getBtnNextTurn().setEnabled(false);
				timer = new Timer();
				TimerTask autoTurn = new TimerTask() {
					@Override
					public void run() {
						model.zugAusf�hren();
					}
				};
				timer.schedule(autoTurn, 0, 1);
				break;
				
			// Automatische Zugausf�hrung stoppen
			case View.ACT_AUTO_OFF:
				view.getBtnSchnellZiehenAn().setEnabled(true);
				view.getBtnSchnellZiehenAus().setEnabled(false);
				view.getBtnNextTurn().setEnabled(true);
				timer.cancel();
				break;
			
		}
	}
	
}
