package Client;

import org.junit.Assert;
import org.junit.Test;
import javafx.util.Pair;
import java.util.ArrayList;

public class IPAddressTest {

    @Test
    public void testIPRegex() {
        // Using this obj for its static functions.
        IPAddress ip = new IPAddress("0.0.0.0");

        ArrayList<Pair<String,Boolean>> ips = new ArrayList<>();

        // Good IP Addresses
        ips.add(new Pair<>("192.168.1.1",true));
        ips.add(new Pair<>("10.0.0.1",true));
        ips.add(new Pair<>("52.36.152.255",true));
        ips.add(new Pair<>("0.12.1.21",true));
        ips.add(new Pair<>("255.255.255.255",true));
        ips.add(new Pair<>("0.0.0.0",true));
        ips.add(new Pair<>("1.1.1.1",true));
        ips.add(new Pair<>("8.8.4.4",true));
        ips.add(new Pair<>("254.254.254.254",true));

        // Bad IP Addresses
        ips.add(new Pair<>("000.0000.00.00",false));
        ips.add(new Pair<>("912.456.123.123",false));
        ips.add(new Pair<>("ip address",false));
        ips.add(new Pair<>("0.0.0.0.0",false));
        ips.add(new Pair<>("0.0.0",false));
        ips.add(new Pair<>("",false));
        ips.add(new Pair<>("0.0.0.256",false));
        ips.add(new Pair<>("256.0.0.0",false));

        // Test the IPs above
        for(Pair<String,Boolean> pair: ips) {
            Assert.assertEquals(ip.isValidIP(pair.getKey()), pair.getValue());
        }
    }
}

