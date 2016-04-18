package no.glv.paco.data.test;

import no.glv.paco.data.CloudSQLDatabase;

/**
 * Created by GleVoll on 18.04.2016.
 */
public class TestSQL {

    public static void main ( String args[] ) throws Exception {

        CloudSQLDatabase database = CloudSQLDatabase.GetInstance("", "paco", "123456" );

    }
}
