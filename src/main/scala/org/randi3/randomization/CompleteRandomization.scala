package org.randi3.randomization

import org.randi3.model._
import org.randi3.randomization._
import org.apache.commons.math3.random._

case class CompleteRandomization(id: Int = Int.MinValue, version:Int = 0)(val random: RandomGenerator) extends RandomizationMethod{

	def randomize(trial: Trial, subject: TrialSubject):TreatmentArm = {
		trial.treatmentArms(random.nextInt(trial.treatmentArms.size))
	}
}

object CompleteRandomization {

}
