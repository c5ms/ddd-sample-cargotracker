package se.citerus.dddsample.domain.model.voyage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.shared.DomainEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A Voyage.
 */
@Entity(name = "Voyage")
@Table(name = "Voyage")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voyage implements DomainEntity<Voyage> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "voyage_number", unique = true)
    private String voyageNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "voyage_id")
    private List<CarrierMovement> carrierMovements;

    private Voyage(final VoyageNumber voyageNumber, final Schedule schedule) {
        Validate.notNull(voyageNumber, "Voyage number is required");
        Validate.notNull(schedule, "Schedule is required");

        this.voyageNumber = voyageNumber.idString();
        this.carrierMovements = schedule.carrierMovements().toList();
    }

    public static Voyage of(final VoyageNumber voyageNumber, final Schedule schedule){
        return new Voyage(voyageNumber, schedule);
    }

    public static Voyage empty(){
        return new Voyage(VoyageNumber.of(""), Schedule.EMPTY);
    }

    /**
     * @return Voyage number.
     */
    public VoyageNumber voyageNumber() {
        return VoyageNumber.of(voyageNumber);
    }

    @Override
    public int hashCode() {
        return voyageNumber.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Voyage that)) return false;
        return sameIdentityAs(that);
    }

    @Override
    public boolean sameIdentityAs(Voyage other) {
        if(null==other){
            return false;
        }
        return this.voyageNumber().sameValueAs(other.voyageNumber());
    }

    @Override
    public String toString() {
        return "Voyage " + voyageNumber;
    }


    /**
     * Builder pattern is used for incremental construction
     * of a Voyage aggregate. This serves as an aggregate factory.
     */
    public static final class Builder {

        private final List<CarrierMovement> carrierMovements = new ArrayList<>();
        private final VoyageNumber voyageNumber;
        private Location departureLocation;

        public Builder(final VoyageNumber voyageNumber, final Location departureLocation) {
            Validate.notNull(voyageNumber, "Voyage number is required");
            Validate.notNull(departureLocation, "Departure location is required");

            this.voyageNumber = voyageNumber;
            this.departureLocation = departureLocation;
        }

        public Builder addMovement(Location arrivalLocation, Instant departureTime, Instant arrivalTime) {
            carrierMovements.add(CarrierMovement.of(departureLocation, arrivalLocation, departureTime, arrivalTime));
            // Next departure location is the same as this arrival location
            this.departureLocation = arrivalLocation;
            return this;
        }

        public Voyage build() {
            return new Voyage(voyageNumber, Schedule.of(carrierMovements));
        }

    }

}
