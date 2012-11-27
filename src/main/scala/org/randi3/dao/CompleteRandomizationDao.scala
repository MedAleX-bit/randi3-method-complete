package org.randi3.dao

import org.randi3.schema.DatabaseSchema._

import org.scalaquery.session.Database
import org.scalaquery.session._
import Database.threadLocalSession
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.extended.ExtendedProfile
import scalaz._

import org.randi3.randomization.CompleteRandomization

class CompleteRandomizationDao(database: Database, driver: ExtendedProfile) extends AbstractRandomizationMethodDao(database, driver) {
  import driver.Implicit._
  import schema._

  def create(randomizationMethod: CompleteRandomization, trialId: Int): Validation[String, Int] = {
    database withSession {
      threadLocalSession withTransaction {
        RandomizationMethods.noId insert (trialId, generateBlob(randomizationMethod.random), randomizationMethod.getClass().getName())
      }
      getId(trialId)
    }

  }

  def get(id: Int): Validation[String, Option[CompleteRandomization]] = {
    database withSession {
      val resultList = queryRandomizationMethodFromId(id).list
      if (resultList.isEmpty) Success(None)
      else if (resultList.size == 1) {
        val rm = resultList(0)
        if (rm._3 == classOf[CompleteRandomization].getName()) {
          Success(Some(new CompleteRandomization(rm._1.get, 0)(deserializeRandomGenerator(rm._2.get))))
        } else {
          Failure("Wrong plugin")
        }

      } else Failure("More than one method with id=" + id + " found")
    }
  }

  def getFromTrialId(trialId: Int): Validation[String, Option[CompleteRandomization]] = {
    database withSession {
      val resultList = queryRandomizationMethodFromTrialId(trialId).list
      if (resultList.isEmpty) Success(None)
      else if (resultList.size == 1) {
        val rm = resultList(0)
        if (rm._4 == classOf[CompleteRandomization].getName()) {
          Success(Some(new CompleteRandomization(rm._1.get, 0)(deserializeRandomGenerator(rm._3.get))))
        } else {
          Failure("Wrong plugin")
        }
      } else Failure("More than one method for trial with id=" + trialId + " found")
    }
  }

  def update(randomizationMethod: CompleteRandomization): Validation[String, CompleteRandomization] = {
    database withSession {
      queryRandomizationMethodFromId(randomizationMethod.id).mutate { r =>
        r.row = r.row.copy(_2 = generateBlob(randomizationMethod.random), _3 = randomizationMethod.getClass().getName())
      }
    }
    
    get(randomizationMethod.id).either match {
      case Left(x) => Failure(x)
      case Right(None) => Failure("Method not found")
      case Right(Some(randomizationMethod)) => Success(randomizationMethod)
    }
  }

  def delete(randomizationMethod: CompleteRandomization) {
    database withSession {
      queryRandomizationMethodFromId(randomizationMethod.id).mutate { r =>
       r.delete()
      }
    }
  }

}
