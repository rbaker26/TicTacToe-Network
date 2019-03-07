package Client;

import org.junit.Assert;
import org.junit.Test;
import javafx.util.Pair;
import java.util.ArrayList;


//************************************************************************************
public class PortWrapperTest {

    //***************************************************************************
    @Test
    public void testisValidPort() {
        PortWrapper pw = new PortWrapper(1);

        ArrayList<Pair<PortWrapper,Boolean>> ports = new ArrayList<>();

        // Good IP Addresses
        ports.add(new Pair<>(new PortWrapper(1),true));
        ports.add(new Pair<>(new PortWrapper(2),true));
        ports.add(new Pair<>(new PortWrapper(3),true));
        ports.add(new Pair<>(new PortWrapper(255),true));
        ports.add(new Pair<>(new PortWrapper(20000),true));
        ports.add(new Pair<>(new PortWrapper(25000),true));
        ports.add(new Pair<>(new PortWrapper(50000),true));
        ports.add(new Pair<>(new PortWrapper(65535),true));

        // Bad IP Addresses
        ports.add(new Pair<>(new PortWrapper(-1),false));
        ports.add(new Pair<>(new PortWrapper(65536),false));
        ports.add(new Pair<>(new PortWrapper(100000),false));
        ports.add(new Pair<>(new PortWrapper(0),false));

        // Test the IPs above
        for(Pair<PortWrapper,Boolean> pair: ports) {
            Assert.assertEquals(pw.isValidPort(pair.getKey()), pair.getValue());
        }
    }
    //***************************************************************************
}
//************************************************************************************
