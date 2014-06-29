package com.openxc.messages;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.openxc.messages.DiagnosticRequest;

import android.os.Parcel;

@Config(emulateSdk = 18, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class DiagnosticRequestTest {
    DiagnosticRequest request;
    int id = 42;
    int bus = 1;
    int mode = 2;
    int pid = 4;
    byte[] payload = new byte[]{1, 2, 3, 4, 5, 6, 7};

    @Before
    public void setup() {
        request = new DiagnosticRequest(bus, id, mode, pid);
        request.setPayload(payload);
    }

    @Test
    public void checkRequiredFields() {
        Set<String> fields = new HashSet<>();
        fields.add(DiagnosticRequest.ID_KEY);
        fields.add(DiagnosticRequest.BUS_KEY);
        fields.add(DiagnosticRequest.MODE_KEY);
        assertTrue(DiagnosticRequest.containsRequiredFields(fields));
    }

    @Test
    public void getIdReturnsId() {
        assertEquals(id, request.getId());
    }

    @Test
    public void getBusReturnsBus() {
        assertEquals(bus, request.getBusId());
    }

    @Test
    public void getModeReturnsMode() {
        assertEquals(mode, request.getMode());
    }

    @Test
    public void getPidWithNoPidReturnsNull() {
        DiagnosticRequest noPidRequest = new DiagnosticRequest(1, 2, 3);
        assertThat(noPidRequest.getPid(), nullValue());
    }

    @Test
    public void getNoNameReturnsNull() {
        assertThat(request.getName(), nullValue());
    }

    @Test
    public void getPayloadReturnsPayload() {
        assertArrayEquals(payload, request.getPayload());
    }

    @Test
    public void sameEquals() {
        assertEquals(request, request);
    }

    @Test
    public void differentIdNotEqual() {
        DiagnosticRequest anotherRequest = new DiagnosticRequest(
                bus, id + 1, mode);
        anotherRequest.setPayload(payload);
        assertThat(request, not(equalTo(anotherRequest)));
    }

    @Test
    public void differentBusNotEqual() {
        DiagnosticRequest anotherRequest = new DiagnosticRequest(
                bus + 1, id, mode);
        anotherRequest.setPayload(payload);
        assertThat(request, not(equalTo(anotherRequest)));
    }

    @Test
    public void differentModeNotEqual() {
        DiagnosticRequest anotherRequest = new DiagnosticRequest(
                bus, id, mode + 1);
        anotherRequest.setPayload(payload);
        assertThat(request, not(equalTo(anotherRequest)));
    }

    @Test
    public void differentPayloadNotEqual() {
        payload[1] = (byte) (payload[1] + 1);
        DiagnosticRequest anotherRequest = new DiagnosticRequest(
                bus, id, mode);
        anotherRequest.setPayload(payload);
        assertThat(request, not(equalTo(anotherRequest)));
    }

    @Test
    public void toStringNotNull() {
        assertThat(request.toString(), notNullValue());
    }

    @Test
    public void writeAndReadFromParcelWithPayloadAndPid() {
        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);

        // Reset parcel for reading
        parcel.setDataPosition(0);

        VehicleMessage createdFromParcel =
                VehicleMessage.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel, instanceOf(DiagnosticRequest.class));
        assertEquals(request, createdFromParcel);
    }

    @Test
    public void writeAndReadFromParcelNoOptionalFields() {
        request = new DiagnosticRequest(1, 2, 3);

        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);

        // Reset parcel for reading
        parcel.setDataPosition(0);

        VehicleMessage createdFromParcel =
                VehicleMessage.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel, instanceOf(DiagnosticRequest.class));
        assertEquals(request, createdFromParcel);
    }

    @Test
    public void keyNotNull() {
        assertThat(request.getKey(), notNullValue());
    }
}
