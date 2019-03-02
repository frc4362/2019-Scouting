package com.gemsrobotics.scouting2019

import scalafx.application.JFXApp
import scalafx.beans.property.IntegerProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{Background, BackgroundFill, GridPane, VBox}

object DataScoutingApp extends JFXApp {
	def userConfirm(question: String): Boolean = {
		val response: Option[ButtonType] =
			new Alert(AlertType.Confirmation, question, ButtonType.Cancel, ButtonType.OK) {
				title = "?"
			}.showAndWait()

		response match {
			case Some(ButtonType.Cancel) =>
				false
			case Some(ButtonType.OK) =>
				true
			case Some(_) =>
				false
			case None =>
				false
		}
	}

	val teamNumberLabel: Label = new Label("Team #")
	val teamNumberTextField: TextField = new TextField
	val matchNumberLabel: Label = new Label("Match #")
	val matchNumberTextField: TextField = new TextField
	val nameLabel: Label = new Label("Name")
	val nameTextField: TextField = new TextField

	val scoredCargoShipIncrementable: Incrementable =
		new Incrementable("Cargo Scored Ship")
	val scoredPanelShipIncrementable: Incrementable =
		new Incrementable("Hatches Scored Ship")

	val cargoIncrementables: Seq[Incrementable] =
		Seq(new Incrementable("Cargo Rocket 1"),
			new Incrementable("Cargo Rocket 2"),
			new Incrementable("Cargo Rocket 3"),
			scoredCargoShipIncrementable)
	val hatchIncrementables: Seq[Incrementable] =
		Seq(new Incrementable("Hatch Rocket 1"),
			new Incrementable("Hatch Rocket 2"),
			new Incrementable("Hatch Rocket 3"),
			scoredPanelShipIncrementable)

	cargoIncrementables.init.flatMap(_.btns).foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.rocketBlue};"
	}
	cargoIncrementables.last.btns.foreach { btn =>
		btn.style.value = btn.style.value + "-fx-background-color: orange;"
	}
	hatchIncrementables.init.flatMap(_.btns).foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.rocketBlue};"
	}
	hatchIncrementables.last.btns.foreach(_.style.value += "-fx-background-color: orange;")

	val climbLevelProperty: IntegerProperty =
		IntegerProperty(0)
	val climbLevelLabel: Label = new Label("Level Climbed") {
		style = "-fx-font: 24px \"Sans\";"
	}

	val climbLevelSlider: Slider = new Slider {
		value <==> climbLevelProperty
		snapToTicks = true
		showTickMarks = true
		showTickLabels = true
		blockIncrement = 1.0
		majorTickUnit = 1.0
		minorTickCount = 0
		max = 3.0
		min = 0.0
	}

	val startingLevelLabel: Label =
		new Label("Line Cross Level") {
			style = "-fx-font: 24px \"Sans\";"
		}
	val startingLevelProperty: IntegerProperty =
		IntegerProperty(0)
	val startingLevelSlider: Slider = new Slider {
		value <==> startingLevelProperty
		snapToTicks = true
		showTickMarks = true
		showTickLabels = true
		blockIncrement = 1.0
		majorTickUnit = 1.0
		minorTickCount = 0
		max = 2.0
		min = 0.0
	}
	val sandstormHatchesIncrementable: Incrementable =
		new Incrementable("Hatches Scored")
	val sandstormPickupsIncrementable: Incrementable =
		new Incrementable("Game Piece Pickups")
	val sandstormCargoIncrementable: Incrementable =
		new Incrementable("Cargo Scored")

	sandstormHatchesIncrementable.btns.foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.sandstormYellow};"
	}
	sandstormCargoIncrementable.btns.foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.sandstormYellow};"
	}
	sandstormPickupsIncrementable.btns.foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.sandstormYellow};"
	}

	val piecesDroppedIncrementable: Incrementable =
		new Incrementable("Drops When Hit")
	val commentsLabel: Label = new Label("Comments") {
		style = "-fx-font: 24px \"Sans\";"
	}

	val resetButton: Button =
		new Button("Reset") {
			prefWidth = 240
			prefHeight = 100
			style = style.value + s"-fx-background-color: ${Components.resetRed}; -fx-font-size: 24px;"
		}

	val saveButton: Button =
		new Button("Save") {
			prefWidth = 240
			prefHeight = 100
			style = style.value + s"-fx-background-color: ${Components.affirmativeGreen}; -fx-font-size: 24px;"
		}

	val record = new DataRecord(
		matchNumberTextField.text,
		teamNumberTextField.text,
		nameTextField.text,
		cargoIncrementables.head.count,
		cargoIncrementables(1).count,
		cargoIncrementables(2).count,
		hatchIncrementables.head.count,
		hatchIncrementables(1).count,
		hatchIncrementables(2).count,
		climbLevelProperty,
		startingLevelProperty,
		sandstormHatchesIncrementable.count,
		sandstormPickupsIncrementable.count,
		piecesDroppedIncrementable.count,
		scoredCargoShipIncrementable.count,
		scoredPanelShipIncrementable.count,
		sandstormCargoIncrementable.count
	)

	resetButton.onMouseClicked = { _ =>
		if (userConfirm("Are you sure you want to reset?\nTHIS IS IRREVERSIBLE")) {
			record.reset()
		}
	}

	saveButton.onMouseClicked = { _ =>
		if (userConfirm("Are you sure you want to save?")) {
			record.save()
		}
	}

	val saveButtons = new VBox {
		spacing = 10
		children = Seq(
			resetButton,
			saveButton
		)
	}

	DataRecord.ensureOutputExists()
	stage = new JFXApp.PrimaryStage {
		title.value = "Destination Deep Space Scouting App"
		minHeight = 630
		minWidth = 970
		maxHeight = 630
		maxWidth = 970
		height = 630
		width = 970
		resizable = false
		scene = new Scene {
			stylesheets.add("stylesheet.css")
			content = new GridPane {
				prefWidth = 970
				prefHeight = 630
				alignment = Pos.BaselineLeft
				hgap = 10
				padding = Insets(10, 10, 10, 10)
				background = new Background(Array(new BackgroundFill(Components.backgroundGrey, null, null)))
				add(teamNumberLabel, 0, 0)
				add(teamNumberTextField, 1, 0, 2, 1)
				add(matchNumberLabel, 3, 0, 1, 1)
				add(matchNumberTextField, 4, 0, 2, 1)
				add(nameLabel, 6, 0, 1, 1)
				add(nameTextField, 7, 0, 2, 1)

				add(startingLevelLabel, 0, 1, 2, 1)
				add(startingLevelSlider, 0, 2, 2, 1)
				add(new Label("Sandstorm"), 0, 3, 2, 1)
				add(sandstormHatchesIncrementable, 0, 4, 2, 2)
				add(sandstormCargoIncrementable, 0, 6, 2, 2)
				add(sandstormPickupsIncrementable, 0, 8, 2, 2)

				add(new Label("Piece Scoring"), 2, 1, 2, 1)
				(1 to 4).foreach(n => add((cargoIncrementables.init.reverse ++ cargoIncrementables.drop(3)).toList(n - 1), 2, n * 2, 2, 2))
				(1 to 4).foreach(n => add((hatchIncrementables.init.reverse ++ hatchIncrementables.drop(3)).toList(n - 1), 4, n * 2, 2, 2))

				add(piecesDroppedIncrementable, 6, 2, 2, 2)
				add(climbLevelLabel, 6, 4, 2, 1)
				add(climbLevelSlider, 6, 5, 2, 1)
				add(saveButtons, 6, 6, 2, 3)
			}
		}
	}

	stage.sizeToScene()
	stage.minWidth = stage.width.value
	stage.minHeight = stage.height.value
}
