package si.uni_lj.fri.pbd.contacts;

import androidx.annotation.NonNull;

public class Contact {
    private long contactId;
    private String contactName;
    private String contactEmailAddress;
    private String contactPhoneNumber;

    public Contact() {
        this.contactName = null;
        this.contactEmailAddress = null;
        this.contactPhoneNumber = null;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactEmailAddress(String contactEmailAddress) {
        this.contactEmailAddress = contactEmailAddress;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public long getContactId() {
        return contactId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(contactName);
        if (contactPhoneNumber != null) {
            builder.append(" " + contactPhoneNumber);
        }
        if (contactEmailAddress != null) {
            builder.append(" " + contactEmailAddress);
        }
        return builder.toString();
    }
}
