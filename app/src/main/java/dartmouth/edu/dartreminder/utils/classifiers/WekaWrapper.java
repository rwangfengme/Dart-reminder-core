// Generated with Weka 3.6.13
//
// This code is public domain and comes with no warranty.
//
// Timestamp: Sun Feb 14 17:46:01 EST 2016

package dartmouth.edu.dartreminder.utils.classifiers;

import weka.classifiers.Classifier;
import weka.core.Capabilities;
import weka.core.CapabilitiesHandler;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;

public class WekaWrapper
        implements Classifier, CapabilitiesHandler {

    /**
     * Returns only the toString() method.
     *
     * @return a string describing the classifier
     */
    public String globalInfo() {
        return toString();
    }

    /**
     * Returns the capabilities of this classifier.
     *
     * @return the capabilities
     */
    public Capabilities getCapabilities() {
        weka.core.Capabilities result = new weka.core.Capabilities(this);

        result.enable(weka.core.Capabilities.Capability.NOMINAL_ATTRIBUTES);
        result.enable(weka.core.Capabilities.Capability.NUMERIC_ATTRIBUTES);
        result.enable(weka.core.Capabilities.Capability.DATE_ATTRIBUTES);
        result.enable(weka.core.Capabilities.Capability.MISSING_VALUES);
        result.enable(weka.core.Capabilities.Capability.NOMINAL_CLASS);
        result.enable(weka.core.Capabilities.Capability.MISSING_CLASS_VALUES);

        result.setMinimumNumberInstances(0);

        return result;
    }

    /**
     * only checks the data against its capabilities.
     *
     * @param i the training data
     */
    public void buildClassifier(Instances i) throws Exception {
        // can classifier handle the data?
        getCapabilities().testWithFail(i);
    }

    /**
     * Classifies the given instance.
     *
     * @param i the instance to classify
     * @return the classification result
     */
    public double classifyInstance(Instance i) throws Exception {
        Object[] s = new Object[i.numAttributes()];

        for (int j = 0; j < s.length; j++) {
            if (!i.isMissing(j)) {
                if (i.attribute(j).isNominal())
                    s[j] = new String(i.stringValue(j));
                else if (i.attribute(j).isNumeric())
                    s[j] = new Double(i.value(j));
            }
        }

        // set class value to missing
        s[i.classIndex()] = null;

        return WekaClassifier.classify(s);
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        return new double[0];
    }

    /**
     * Returns the revision string.
     *
     * @return        the revision
     */
    public String getRevision() {
        return RevisionUtils.extract("1.0");
    }

    /**
     * Returns only the classnames and what classifier it is based on.
     *
     * @return a short description
     */
    public String toString() {
        return "Auto-generated classifier wrapper, based on weka.classifiers.trees.J48 (generated with Weka 3.6.13).\n" + this.getClass().getName() + "/WekaClassifier";
    }

    /**
     * Runs the classfier from commandline.
     *
     * @param args the commandline arguments
     */
    /*public static void main(String args[]) {
        runClassifier(new WekaWrapper(), args);
    }*/
}

class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N4c2bac4f0(i);
        return p;
    }
    static double N4c2bac4f0(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 90.593144) {
            p = WekaClassifier.N473d9d9a1(i);
        } else if (((Double) i[0]).doubleValue() > 90.593144) {
            p = WekaClassifier.N7cc4613b8(i);
        }
        return p;
    }
    static double N473d9d9a1(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 0;
        } else if (((Double) i[64]).doubleValue() <= 1.59406) {
            p = 0;
        } else if (((Double) i[64]).doubleValue() > 1.59406) {
            p = WekaClassifier.N1e360e002(i);
        }
        return p;
    }
    static double N1e360e002(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 0;
        } else if (((Double) i[5]).doubleValue() <= 5.133591) {
            p = WekaClassifier.N6ed1317e3(i);
        } else if (((Double) i[5]).doubleValue() > 5.133591) {
            p = WekaClassifier.N398d8f717(i);
        }
        return p;
    }
    static double N6ed1317e3(Object []i) {
        double p = Double.NaN;
        if (i[29] == null) {
            p = 0;
        } else if (((Double) i[29]).doubleValue() <= 0.471342) {
            p = WekaClassifier.N680880084(i);
        } else if (((Double) i[29]).doubleValue() > 0.471342) {
            p = 0;
        }
        return p;
    }
    static double N680880084(Object []i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 0;
        } else if (((Double) i[14]).doubleValue() <= 1.201922) {
            p = WekaClassifier.N60dc2945(i);
        } else if (((Double) i[14]).doubleValue() > 1.201922) {
            p = 1;
        }
        return p;
    }
    static double N60dc2945(Object []i) {
        double p = Double.NaN;
        if (i[23] == null) {
            p = 0;
        } else if (((Double) i[23]).doubleValue() <= 0.312341) {
            p = WekaClassifier.N716185a66(i);
        } else if (((Double) i[23]).doubleValue() > 0.312341) {
            p = 0;
        }
        return p;
    }
    static double N716185a66(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 73.150691) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 73.150691) {
            p = 1;
        }
        return p;
    }
    static double N398d8f717(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() <= 6.069093) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() > 6.069093) {
            p = 0;
        }
        return p;
    }
    static double N7cc4613b8(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 555.416673) {
            p = WekaClassifier.N40cba87b9(i);
        } else if (((Double) i[0]).doubleValue() > 555.416673) {
            p = 2;
        }
        return p;
    }
    static double N40cba87b9(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 150.890123) {
            p = WekaClassifier.N7aa2447810(i);
        } else if (((Double) i[0]).doubleValue() > 150.890123) {
            p = WekaClassifier.N270143a717(i);
        }
        return p;
    }
    static double N7aa2447810(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 24.448736) {
            p = WekaClassifier.N333d612e11(i);
        } else if (((Double) i[2]).doubleValue() > 24.448736) {
            p = WekaClassifier.N52bdbdf116(i);
        }
        return p;
    }
    static double N333d612e11(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 0;
        } else if (((Double) i[11]).doubleValue() <= 0.61409) {
            p = WekaClassifier.N2b72d4a12(i);
        } else if (((Double) i[11]).doubleValue() > 0.61409) {
            p = WekaClassifier.Naebd7d314(i);
        }
        return p;
    }
    static double N2b72d4a12(Object []i) {
        double p = Double.NaN;
        if (i[29] == null) {
            p = 1;
        } else if (((Double) i[29]).doubleValue() <= 0.324032) {
            p = 1;
        } else if (((Double) i[29]).doubleValue() > 0.324032) {
            p = WekaClassifier.N47fe56aa13(i);
        }
        return p;
    }
    static double N47fe56aa13(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() <= 5.480874) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() > 5.480874) {
            p = 2;
        }
        return p;
    }
    static double Naebd7d314(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 5.465722) {
            p = WekaClassifier.N1a6633f15(i);
        } else if (((Double) i[5]).doubleValue() > 5.465722) {
            p = 1;
        }
        return p;
    }
    static double N1a6633f15(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 6.599083) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 6.599083) {
            p = 0;
        }
        return p;
    }
    static double N52bdbdf116(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 12.44154) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() > 12.44154) {
            p = 0;
        }
        return p;
    }
    static double N270143a717(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 400.697807) {
            p = WekaClassifier.N44009cf218(i);
        } else if (((Double) i[0]).doubleValue() > 400.697807) {
            p = WekaClassifier.N3257ab8c21(i);
        }
        return p;
    }
    static double N44009cf218(Object []i) {
        double p = Double.NaN;
        if (i[29] == null) {
            p = 1;
        } else if (((Double) i[29]).doubleValue() <= 4.516569) {
            p = 1;
        } else if (((Double) i[29]).doubleValue() > 4.516569) {
            p = WekaClassifier.N43ff6bf19(i);
        }
        return p;
    }
    static double N43ff6bf19(Object []i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 2;
        } else if (((Double) i[8]).doubleValue() <= 6.311196) {
            p = 2;
        } else if (((Double) i[8]).doubleValue() > 6.311196) {
            p = WekaClassifier.N36077c720(i);
        }
        return p;
    }
    static double N36077c720(Object []i) {
        double p = Double.NaN;
        if (i[17] == null) {
            p = 2;
        } else if (((Double) i[17]).doubleValue() <= 2.254351) {
            p = 2;
        } else if (((Double) i[17]).doubleValue() > 2.254351) {
            p = 1;
        }
        return p;
    }
    static double N3257ab8c21(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 46.108642) {
            p = WekaClassifier.N19bf8e2622(i);
        } else if (((Double) i[5]).doubleValue() > 46.108642) {
            p = 1;
        }
        return p;
    }
    static double N19bf8e2622(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (((Double) i[4]).doubleValue() <= 64.120766) {
            p = WekaClassifier.N6f0d6dfe23(i);
        } else if (((Double) i[4]).doubleValue() > 64.120766) {
            p = 1;
        }
        return p;
    }
    static double N6f0d6dfe23(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 2.136024) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() > 2.136024) {
            p = WekaClassifier.N5ab077a724(i);
        }
        return p;
    }
    static double N5ab077a724(Object []i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 2;
        } else if (((Double) i[8]).doubleValue() <= 7.718595) {
            p = 2;
        } else if (((Double) i[8]).doubleValue() > 7.718595) {
            p = WekaClassifier.N777fa37425(i);
        }
        return p;
    }
    static double N777fa37425(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 15.444108) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 15.444108) {
            p = WekaClassifier.N5cc451f626(i);
        }
        return p;
    }
    static double N5cc451f626(Object []i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() <= 12.563103) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() > 12.563103) {
            p = 2;
        }
        return p;
    }
}
