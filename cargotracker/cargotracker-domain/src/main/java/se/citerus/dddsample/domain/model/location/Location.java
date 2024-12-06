package se.citerus.dddsample.domain.model.location;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.shared.DomainEntity;

/**
 * A location is our model is stops on a journey, such as cargo
 * origin or destination, or carrier movement endpoints.
 * It is uniquely identified by a UN Locode.
 */
@Entity(name = "Location")
@Table(name = "Location")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location implements DomainEntity<Location> {

    public static final Location UNKNOWN = new Location("XXXXX", "Unknown location");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String unlocode;

    @Column(nullable = false)
    private String name;

    private Location(String unlocode, String name) {
        this.unlocode = unlocode;
        this.name = name;
    }

    /**
     * Package-level constructor, visible for test and sample data purposes.
     *
     * @param unLocode UN Locode
     * @param name     location name
     * @throws IllegalArgumentException if the UN Locode or name is null
     */
    private Location(final UnLocode unLocode, final String name) {
        Validate.notNull(unLocode, "unLocode can not be null");
        Validate.notNull(name, "name can not be null");

        this.unlocode = unLocode.idString();
        this.name = name;
    }

    public static Location of(final UnLocode unLocode, final String name) {
        return new Location(unLocode, name);
    }

    /**
     * @param object to compare
     * @return Since this is an entiy this will be true iff UN locodes are equal.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof Location other)) {
            return false;
        }
        return sameIdentityAs(other);
    }

    @Override
    public boolean sameIdentityAs(final Location other) {
        if (null == other) {
            return false;
        }
        return this.unlocode.equals(other.unlocode);
    }

    /**
     * @return Hash code of UN locode.
     */
    @Override
    public int hashCode() {
        return unlocode.hashCode();
    }

    @Override
    public String toString() {
        return name + " [" + unlocode + "]";
    }

}
