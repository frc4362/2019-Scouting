import javafx.scene.text.FontWeight
import scalafx.application.JFXApp
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, CheckBox, Label, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Font

object ProofOfConcept extends JFXApp {
	stage = new JFXApp.PrimaryStage {
		title.value = "Destination Deep Space Scouting App"
		scene = new Scene {
			content = new VBox {
				spacing = 20
				padding = Insets(10, 10, 10, 10)
				children = Seq(
					new HBox {
						spacing = 15
						children = Seq(
							new Incrementable("Test Value 1"),
							new Incrementable("Test Value 2"))
					},
					new HBox {
						spacing = 15
						children = Seq(
							new Incrementable("Test Value 3"),
							new Incrementable("Test Value 4"))
					},
					new CheckBox {
						text = "Test Question?"
						font = Font("Tahoma", FontWeight.NORMAL, 24)
					},
					new HBox {
						spacing = 15
						children = Seq(
							new Label("Short Answer:") {
								font = Font("Tahoma", FontWeight.NORMAL, 18)
							},
							new TextField {
								prefWidth = 310
							}
						)
					},
					new HBox {
						spacing = 15
						children = Seq(
							new Button("Save") {
								prefWidth = 200
								prefHeight = 100
								font = Font("Tahoma", FontWeight.NORMAL, 40)
							},
							new Button("Clear") {
								prefWidth = 200
								prefHeight = 100
								font = Font("Tahoma", FontWeight.NORMAL, 40)
							}
						)
					}
				)
			}
		}
	}

	stage.sizeToScene()
	stage.minWidth = stage.width.value
	stage.minHeight = stage.height.value
}
