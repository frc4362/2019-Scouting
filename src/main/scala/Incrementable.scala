import scalafx.Includes.handle
import scalafx.beans.property.IntegerProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{Font, FontWeight, TextAlignment}

private class MyButton(text: String) extends Button(text) {
	prefWidth = 100
	prefHeight = 50
	font = Font("Consolas", FontWeight.Normal, 30)
}

class Incrementable(
     name: String,
     startingValue: Int = 0
) extends VBox {
	private val countLabel = new Label(startingValue.toString) {
		font = Font("Tahoma", FontWeight.Normal, 48)
		alignment = Pos.BaselineCenter
		textAlignment = TextAlignment.Center
		prefWidth = 215
	}

	private val nameLabel = new Label(name) {
		font = Font("Tahoma", FontWeight.Normal, 24)
	}

	val count = IntegerProperty(0)
	count.onChange { (_, _, newValue) =>
		countLabel.text = newValue.toString
	}

	private val buttons = new HBox {
		spacing = 10
		children = Seq(
			new MyButton("-") {
				onMouseClicked = handle {
					if (count.value > 0) {
						count.value -= 1
					}
				}
			},
			new MyButton("+ ") {
				onMouseClicked = handle {
					count.value += 1
				}
			}
		)
	}

	children = Seq(
		nameLabel,
		countLabel,
		buttons
	)
}
