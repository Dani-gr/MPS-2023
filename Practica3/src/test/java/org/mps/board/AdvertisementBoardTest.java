package org.mps.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class AdvertisementBoardTest {
    private AdvertisementBoard board;

    @BeforeEach
    void setUp() {
        board = new AdvertisementBoard();
    }

    @Nested
    @DisplayName("Given the AdvertisementBoard constructor")
    class constructor {
        @Test
        @DisplayName("1. when the constructor is called, then there's an advertisement on the board.")
        void thereIsOneAdvertisementAfterCreation() {
            int expected = 1;
            assertEquals(expected, board.numberOfPublishedAdvertisements());
        }
    }

    @Nested
    @DisplayName("Given the board has been created and the method publish()")
    class publish {
        @Test
        @DisplayName("2. when the owner tries to publish an advertisement, " +
                "then the number of advertisements in the board increases.")
        void ownerPublishes() {
            Advertisement ad = new Advertisement("title", "text", AdvertisementBoard.BOARD_OWNER);

            AdvertiserDatabase db_Fake = advertiserName -> true;
            PaymentGateway gateway_Fake = new PaymentGateway() {
                @Override
                public boolean advertiserHasFunds(String advertiserName) {
                    return true;
                }
                @Override
                public void chargeAdvertiser(String advertiserName) {
                }
            };
            assertDoesNotThrow(() -> board.publish(ad, db_Fake, gateway_Fake));

            int expected = 2;
            assertEquals(expected, board.numberOfPublishedAdvertisements());
        }

        @Test
        @DisplayName("3. when a registered advertiser tries to publish an advertisement but has not funds, " +
                "then the number of advertisements stays the same.")
        void advertiserIsRegisteredButHasInsuficientFunds() {
            String advertiser = "Pepe Gotera y Otilio";
            Advertisement ad = new Advertisement("title", "text", advertiser);
            AdvertiserDatabase db = Mockito.mock(AdvertiserDatabase.class);
            PaymentGateway gateway = Mockito.mock(PaymentGateway.class);

            Mockito.when(db.advertiserIsRegistered(advertiser)).thenReturn(true);
            Mockito.when(gateway.advertiserHasFunds(advertiser)).thenReturn(false);

            board.publish(ad, db, gateway);

            int expected = 1;
            assertEquals(expected, board.numberOfPublishedAdvertisements());
        }

        @Test
        @DisplayName("4. when a registered advertiser tries to publish an advertisement and has funds, " +
                "then the number of advertisements increases and they get charged.")
        void advertiserIsRegisteredAndHasFunds() {
            String advertiser = "Robin Robot";
            Advertisement ad = new Advertisement("title", "text", advertiser);
            AdvertiserDatabase db = Mockito.mock(AdvertiserDatabase.class);
            PaymentGateway gateway = Mockito.mock(PaymentGateway.class);

            Mockito.when(db.advertiserIsRegistered(advertiser)).thenReturn(true);
            Mockito.when(gateway.advertiserHasFunds(advertiser)).thenReturn(true);

            board.publish(ad, db, gateway);

            int expected = 2;
            assertEquals(expected, board.numberOfPublishedAdvertisements());
            Mockito.verify(gateway).chargeAdvertiser(advertiser);
        }

        @Test
        @DisplayName("6. when a registered advertiser tries to publish an advertisement which already exists, " +
                "then the number of advertisements stays the same and they don't get charged.")
        void adAlreadyExists() {
            String advertiser = "Robin Robot";
            Advertisement ad1 = new Advertisement("title", "text", advertiser);
            Advertisement ad2 = new Advertisement("title", "text2", advertiser);
            AdvertiserDatabase db = Mockito.mock(AdvertiserDatabase.class);
            PaymentGateway gateway = Mockito.mock(PaymentGateway.class);

            Mockito.when(db.advertiserIsRegistered(advertiser)).thenReturn(true);
            Mockito.when(gateway.advertiserHasFunds(advertiser)).thenReturn(true);

            board.publish(ad1, db, gateway);
            board.publish(ad2, db, gateway);

            int expected = 2;
            assertEquals(expected, board.numberOfPublishedAdvertisements());
            Mockito.verify(gateway, Mockito.atMostOnce()).chargeAdvertiser(advertiser);
        }

        // El uso de un Mock.spy es equivalente; las operaciones se realizan en el spy, así como la comprobación.
        @Test
        @DisplayName("7. when a registered advertiser tries to publish an advertisement when the board is full, " +
                "then a AdvertisementBoardException is thrown.")
        void boardIsFull() {
            String advertiser = "Robin Robot", owner = AdvertisementBoard.BOARD_OWNER;
            Advertisement ad = new Advertisement("title", "text", advertiser);
            AdvertiserDatabase db_Fake = advertiserName -> true;
            PaymentGateway gateway_Fake = new PaymentGateway() {
                @Override
                public boolean advertiserHasFunds(String advertiserName) {
                    return true;
                }
                @Override
                public void chargeAdvertiser(String advertiserName) {
                }
            };
            for (int i = 1; i < AdvertisementBoard.MAX_BOARD_SIZE; i++)
                board.publish(new Advertisement(
                        "test " + i, "text", owner
                ), db_Fake, gateway_Fake);

            assertThrows(AdvertisementBoardException.class,
                    () -> board.publish(ad, db_Fake, gateway_Fake));
        }
    }

    @Nested
    @DisplayName("Given the board has been created and the method findByTitle()")
    class findByTitle {
        @Test
        @DisplayName("5. when the owner tries to search an advertisement that has been deleted, " +
                "then it is not found.")
        void ownerPublishesTwoAdsThenDeletesTheFirstOne() {
            Advertisement ad1 = new Advertisement("title1", "text1", AdvertisementBoard.BOARD_OWNER),
                    ad2 = new Advertisement("title2", "text2", AdvertisementBoard.BOARD_OWNER);
            AdvertiserDatabase db_Fake = advertiserName -> true;
            PaymentGateway gateway_Fake = new PaymentGateway() {
                @Override
                public boolean advertiserHasFunds(String advertiserName) {
                    return true;
                }
                @Override
                public void chargeAdvertiser(String advertiserName) {
                }
            };

            board.publish(ad1, db_Fake, gateway_Fake);
            board.publish(ad2, db_Fake, gateway_Fake);

            board.deleteAdvertisement("title1", AdvertisementBoard.BOARD_OWNER);

            var expected = Optional.empty();
            assertEquals(expected, board.findByTitle("title1"));
        }
    }
}