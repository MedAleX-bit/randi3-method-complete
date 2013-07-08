package org.randi3.randomization


import org.randi3.randomization.configuration._
import org.randi3.dao.CompleteRandomizationDao
import org.randi3.model._
import org.randi3.model.criterion.constraint.Constraint
import criterion.Criterion
import scala.slick.session.Database

import org.apache.commons.math3.random._
import scalaz._

import org.randi3.utility.{I18NHelper, AbstractSecurityUtil}
import org.randi3.utility.I18NRandomization
import scala.slick.driver.ExtendedProfile
import scala.slick.lifted.DDL

class CompleteRandomizationPlugin(database: Database, driver: ExtendedProfile, securityUtil: AbstractSecurityUtil) extends RandomizationMethodPlugin(database, driver, securityUtil){


  private val i18n = new I18NRandomization(I18NHelper.getLocalizationMap("completeRandomizationM", getClass.getClassLoader), securityUtil)

  val name = classOf[CompleteRandomization].getName

  def i18nName = i18n.text("name")

  def description = i18n.text("description")

  val canBeUsedWithStratification = false

  private val completeRandomizationDao = new CompleteRandomizationDao(database, driver)

  def randomizationConfigurationOptions(): (List[ConfigurationType[Any]], Map[String, List[Criterion[_ <: Any, Constraint[_ <: Any]]]])= {
   (Nil, Map())
  }

  def getRandomizationConfigurations(id: Int): List[ConfigurationProperty[Any]] = {
    Nil
  }

  def randomizationMethod(random: RandomGenerator, trial: Trial, configuration: List[ConfigurationProperty[Any]]): Validation[String, RandomizationMethod] = {
    Success(new CompleteRandomization()(random = random))
  }

  def databaseTables(): Option[DDL] = {
    None
  }

  def updateDatabase() {
    //Nothing to do
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
