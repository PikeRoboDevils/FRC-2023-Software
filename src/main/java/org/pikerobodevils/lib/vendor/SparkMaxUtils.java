/* Copyright 2023 Pike RoboDevils, FRC Team 1018
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.md file or
 * at https://opensource.org/licenses/MIT. */

package org.pikerobodevils.lib.vendor;

/*
 * Adapted from 3005's 2022 Code
 * Original source published at https://github.com/FRC3005/Rapid-React-2022-Public/tree/d499655448ed592c85f9cfbbd78336d8841f46e2
 */

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;

public class SparkMaxUtils {

  /**
   * @param error API return value
   * @return
   */
  public static int check(REVLibError error) {
    return error == REVLibError.kOk ? 0 : 1;
  }

  public static REVLibError setDefaultsForNeo(CANSparkMax sparkMax) {
    REVLibError error = REVLibError.kOk;

    sparkMax.setSmartCurrentLimit(60);
    sparkMax.setSecondaryCurrentLimit(90);

    return error;
  }

  public static REVLibError setDefaultsForNeo500(CANSparkMax sparkMax) {
    // TODO: Use this
    REVLibError error = REVLibError.kOk;

    sparkMax.setSmartCurrentLimit(25);
    sparkMax.setSecondaryCurrentLimit(35);

    return error;
  }

  public static class UnitConversions {
    public static REVLibError setDegreesFromGearRatio(
        RelativeEncoder sparkMaxEncoder, double ratio) {
      double degreesPerRotation = 360.0 / ratio;
      double degreesPerRotationPerSecond = degreesPerRotation / 60.0;
      REVLibError error = sparkMaxEncoder.setPositionConversionFactor(degreesPerRotation);

      if (error != REVLibError.kOk) {
        return error;
      }

      return sparkMaxEncoder.setVelocityConversionFactor(degreesPerRotationPerSecond);
    }

    public static REVLibError setRadsFromGearRatio(RelativeEncoder sparkMaxEncoder, double ratio) {
      double radsPerRotation = (2.0 * Math.PI) / ratio;
      double radsPerRotationPerSecond = radsPerRotation / 60.0;
      REVLibError error = sparkMaxEncoder.setPositionConversionFactor(radsPerRotation);

      if (error != REVLibError.kOk) {
        return error;
      }

      return sparkMaxEncoder.setVelocityConversionFactor(radsPerRotationPerSecond);
    }
  }

  public static String faultWordToString(short faults) {
    if (faults == 0) {
      return "";
    }

    StringBuilder builder = new StringBuilder();
    int faultsInt = faults;
    for (int i = 0; i < 16; i++) {
      if (((1 << i) & faultsInt) != 0) {
        builder.append(CANSparkMax.FaultID.fromId(i).toString());
        builder.append(" ");
      }
    }
    return builder.toString();
  }
}
