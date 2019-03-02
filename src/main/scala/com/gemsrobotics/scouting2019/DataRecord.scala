package com.gemsrobotics.scouting2019

import java.nio.file.{Files, Path, Paths}

import com.github.tototoshi.csv.{CSVFormat, CSVWriter, QUOTE_ALL, Quoting}
import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import scala.util.Try
import scala.collection.JavaConverters._
import scala.collection.mutable.MutableList

object DataRecord {
	val SCHEMA_VERSION: String =
		"1.0"

	private val HEADER: String =
		""""Schema Version","Scout Name","Team Number","Match Number","Cargo Level 1","Cargo Level 2","Cargo Level 3","Cargo Cargo Ship","Level Climbed","Hatch Panel Level 1","Hatch Panel Level 2","Hatch Panel Level 3","Hatch Panel Cargo Ship","Sandstorm Bonus Level","Sandstorm Hatch Place","Sandstorm Cargo Place","Sandstorm Game Piece Pickup","Half Cycle","Drops under defense","Match Cargo Group","HP Group Score","Sand Storm Points","Hab Climb Group""""

	private val OUTPUT_PATH: Path =
		Paths.get(System.getProperty("user.dir") + "\\gemscoutput.csv")

	private implicit val format: CSVFormat =
		new MyCSVFormat

	val writer: CSVWriter =
		CSVWriter.open(OUTPUT_PATH.toFile, append=true)(format)

	def ensureOutputExists(): Unit = {
		val lines = Files.readAllLines(OUTPUT_PATH).asScala
		if (lines.headOption.getOrElse("") != HEADER) {
			println("Header not recognized!")
			val preprendedLines = (HEADER ++ "\r\n" ++ lines.mkString("\r\n")).getBytes
			Files.write(OUTPUT_PATH, preprendedLines)
		} else {
			println("File found and has header! Ready to store!")
		}
	}
}

class DataRecord(
    matchNumber: StringProperty,
    teamNumber: StringProperty,
    scoutName: StringProperty,
    scoredCargo1: IntegerProperty,
    scoredCargo2: IntegerProperty,
    scoredCargo3: IntegerProperty,
    scoredPanels1: IntegerProperty,
    scoredPanels2: IntegerProperty,
    scoredPanels3: IntegerProperty,
    climbLevel: IntegerProperty,
    sandstormStartingLevel: IntegerProperty,
    sandstormHatchPlace: IntegerProperty,
    sandstormPickup: IntegerProperty,
    defendedDrops: IntegerProperty,

    scoredCargoShip: IntegerProperty,
		scoredPanelShip: IntegerProperty,
    sandstormCargoPlace: IntegerProperty
) {
	import DataRecord._
	import Utils.bool2Int

	def reset(): Unit = {
		matchNumber.value = Try(matchNumber.value.toInt)
				.toOption
				.map(_ + 1)
  			.map(_.toString)
  			.getOrElse("")
		teamNumber.value = ""
		scoredCargo1.value = 0
		scoredCargo2.value = 0
		scoredCargo3.value = 0
		scoredPanels1.value = 0
		scoredPanels2.value = 0
		scoredPanels3.value = 0
		climbLevel.value = 0
		sandstormStartingLevel.value = 0
		sandstormHatchPlace.value = 0
		sandstormPickup.value = 0
		defendedDrops.value = 0

		sandstormCargoPlace.value = 0
		scoredCargoShip.value = 0
		scoredPanelShip.value = 0
	}

	def isValid: Boolean =
		(matchNumber.isNotEmpty and teamNumber.isNotEmpty and scoutName.isNotEmpty).get

	def save(): Unit = {
		val problems: MutableList[String] = MutableList.empty[String]

		val matchNum = matchNumber.get
		val teamNum = teamNumber.get
		val name = scoutName.get

		if (matchNum.isEmpty) {
			problems += "Match number not specified"
		}

		if (teamNum.isEmpty) {
			problems += "Team number not specified"
		}

		if (name.isEmpty) {
			problems += "Scout name unspecified"
		}

		if (problems.isEmpty) {
			val halfCycle = (sandstormPickup.get ==
				(sandstormCargoPlace.get + sandstormHatchPlace.get)).toInt

			val sandstormPoints =
				4 * sandstormStartingLevel.get.toInt
				  + sandstormHatchPlace.get * 5
					+ sandstormCargoPlace.get * 3
					+ halfCycle

			val hpGroupScore = Seq(
				scoredPanels1.get,
				scoredPanels2.get,
				scoredPanels3.get,
				scoredPanelShip.get
			).sum

			val cargoGroupScore = Seq(
				scoredCargo1.get,
				scoredCargo2.get,
				scoredCargo3.get,
				scoredCargoShip.get
			).sum

			val habClimbGroup = (climbLevel.get > 0).toInt

			val fields = Seq(
				SCHEMA_VERSION,
				name,
				teamNum,
				matchNum,

				scoredCargo1.get,
				scoredCargo2.get,
				scoredCargo3.get,
				scoredCargoShip.get,

				climbLevel.get,

				scoredPanels1.get,
				scoredPanels2.get,
				scoredPanels3.get,
				scoredPanelShip.get,

				sandstormStartingLevel.get,
				sandstormHatchPlace.get,
				sandstormCargoPlace.get,
				sandstormPickup.get,

				halfCycle,
				defendedDrops.get,
				cargoGroupScore,
				hpGroupScore,
				sandstormPoints,
				habClimbGroup
			)

			writer.writeRow(fields)
			reset()
		} else {
			val content = "Missing required fields!\n" + problems.map(" - " ++ _ ++ "\n").mkString("")
			new Alert(AlertType.Error, content).showAndWait()
		}
	}
}
