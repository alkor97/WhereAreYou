package info.alkor.whereareyou.location.minimal;

/**
 * Format of minimal location description.
 * Created by Marlena on 2017-06-12.
 */
class MinimalLocationFormat {
    enum Field {

        DATE(0, true), PROVIDER(1, true), LATITUDE(2, true), LONGITUDE(3, true),
        ALTITUDE(4, false), ACCURACY(5, false), BEARING(6, false), SPEED(7, false);

        private final int position;
        private final boolean mandatory;

        Field(int position, boolean mandatory) {
            this.position = position;
            this.mandatory = mandatory;
        }

        public int getPosition() {
            return position;
        }

        public boolean isMandatory() {
            return mandatory;
        }
    }
}
