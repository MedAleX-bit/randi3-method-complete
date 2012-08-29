package org.randi3.randomization

import org.randi3.randomization._
import org.randi3.randomization.configuration._
import org.randi3.dao.CompleteRandomizationDao
import org.randi3.model._
import org.randi3.model.criterion.Criterion
import org.randi3.model.criterion.constraint.Constraint
import criterion.Criterion
import org.scalaquery.ql._
import org.scalaquery.ql.extended.ExtendedProfile
import org.scalaquery.session.Database

import org.apache.commons.math3.random._
import scalaz._

class CompleteRandomizationPlugin(database: Database, driver: ExtendedProfile) extends RandomizationMethodPlugin(database, driver) {

  val name = CompleteRandomization.getClass.getName.substring(0,  CompleteRandomization.getClass.getName.size - 1)

  val i18nName = name

  val description = "Simple complete randomization method (ignores stratification options and different treatment proportions)"

  val canBeUsedWithStratification = false

  private val completeRandomizationDao = new CompleteRandomizationDao(database, driver)

  def randomizationConfigurationOptions(): (List[ConfigurationType[Any]], List[Criterion[_ <: Any, Constraint[_ <: Any]]])= {
   (Nil, Nil)
  }

  def getRandomizationConfigurations: List[ConfigurationProperty[Any]] = {
    Nil
  }

  def randomizationMethod(random: RandomGenerator, trial: Trial, configuration: List[ConfigurationProperty[Any]]): Validation[String, RandomizationMethod] = {
    Success(new CompleteRandomization(random = random))
  }

  def databaseTables(): Option[DDL] = {
    None
  }

  def create(randomizationMethod: RandomizationMethod, trialId: Int): Validation[String, Int] = {
    completeRandomizationDao.create(randomizationMethod.asInstanceOf[CompleteRandomization], trialId)
  }

  def get(id: Int): Validation[String, Option[RandomizationMethod]] = {
    completeRandomizationDao.get(id)
  }

  def getFromTrialId(trialId: Int):  Validation[String, Option[RandomizationMethod]] = { 
    completeRandomizationDao.getFromTrialId(trialId)
  }

  def update(randomizationMethod: RandomizationMethod): Validation[String, RandomizationMethod] = {
    completeRandomizationDao.update(randomizationMethod.asInstanceOf[CompleteRandomization])
  }

  def delete(randomizationMethod: RandomizationMethod) {
    completeRandomizationDao.delete(randomizationMethod.asInstanceOf[CompleteRandomization])
  }

}
