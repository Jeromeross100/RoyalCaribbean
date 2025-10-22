// feature-offers/royal-offers-graphql/index.js
// Updated to bind on 0.0.0.0 so the Android emulator (10.0.2.2) can reach it.

import { createSchema, createYoga } from "graphql-yoga";
import { createServer } from "node:http";
import { nanoid } from "nanoid";

// In-memory offers
const offers = [
  {
    id: "1",
    title: "Bahamas Getaway",
    image: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
    shortDescription: "3-Night Bahamas cruise from Miami",
    price: "299",
    description: "Sail to Nassau with onboard dining, shows, and family activities.",
    itinerary: "Miami → Nassau → Perfect Day at CocoCay → Miami",
  },
  {
    id: "2",
    title: "Eastern Caribbean Explorer",
    image: "https://images.unsplash.com/photo-1500375592092-40eb2168fd21",
    shortDescription: "7-Night Eastern Caribbean from Port Canaveral",
    price: "799",
    description: "Beach days and crystal waters across the Eastern Caribbean.",
    itinerary:
      "Port Canaveral → St. Thomas → St. Maarten → Perfect Day → Port Canaveral",
  },
  {
    id: "3",
    title: "Western Caribbean Adventure",
    image: "https://images.unsplash.com/photo-1504608524841-42fe6f032b4b",
    shortDescription: "5-Night Western Caribbean from Tampa",
    price: "559",
    description:
      "Snorkel reefs and explore Mayan ruins with guided shore excursions.",
    itinerary: "Tampa → Cozumel → Costa Maya → Tampa",
  },
  {
    id: "4",
    title: "Jamaica & Cayman Escape",
    image: "https://images.unsplash.com/photo-1526481280698-8fcc13fd6ae0",
    shortDescription: "6-Night from Miami",
    price: "689",
    description: "Waterfalls in Ocho Rios and beaches in Grand Cayman.",
    itinerary:
      "Miami → Ocho Rios → George Town (Grand Cayman) → Perfect Day → Miami",
  },
  {
    id: "5",
    title: "Mexico Cozumel Special",
    image: "https://images.unsplash.com/photo-1526483360412-f4dbaf036963",
    shortDescription: "4-Night from Galveston",
    price: "349",
    description: "Cozumel reefs, tacos, and historic sites.",
    itinerary: "Galveston → Cozumel → Sea Day → Galveston",
  },
  {
    id: "6",
    title: "Haiti Beach Break",
    image: "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
    shortDescription: "5-Night from Miami",
    price: "529",
    description: "Private beach vibes and zip-lines.",
    itinerary: "Miami → Labadee (Haiti) → Sea Day → Nassau → Miami",
  },
  {
    id: "7",
    title: "Southern Caribbean Highlights",
    image: "https://images.unsplash.com/photo-1501785888041-af3ef285b470",
    shortDescription: "8-Night from San Juan",
    price: "999",
    description: "Island-hopping through emerald waters.",
    itinerary:
      "San Juan → St. Kitts → Antigua → St. Lucia → Barbados → San Juan",
  },
  {
    id: "8",
    title: "Perfect Day Weekend",
    image: "https://images.unsplash.com/photo-1493558103817-58b2924bce98",
    shortDescription: "3-Night from Port Canaveral",
    price: "279",
    description: "Quick escape with slides, pools, and beach cabanas.",
    itinerary:
      "Port Canaveral → Perfect Day at CocoCay → Nassau → Port Canaveral",
  },
];

// In-memory bookings
const bookings = [];

const typeDefs = /* GraphQL */ `
  type Offer {
    id: ID!
    title: String!
    image: String
    shortDescription: String!
    price: String!
    description: String!
    itinerary: String!
  }

  type Booking {
    id: ID!
    offerId: ID!
    guestName: String!
    email: String!
    createdAt: String!
    confirmationId: String!
  }

  type BookingResult {
    ok: Boolean!
    message: String!
    booking: Booking
  }

  type Query {
    offers: [Offer!]!
    offer(id: ID!): Offer
  }

  type Mutation {
    bookNow(offerId: ID!, guestName: String!, email: String!): BookingResult!
  }
`;

function isValidEmail(e) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(e);
}

const resolvers = {
  Query: {
    offers: () => offers,
    offer: (_, { id }) => offers.find((o) => o.id === id) || null,
  },
  Mutation: {
    bookNow: (_, { offerId, guestName, email }) => {
      const offer = offers.find((o) => o.id === offerId);
      if (!offer) return { ok: false, message: "Offer not found", booking: null };
      if (!guestName.trim())
        return { ok: false, message: "Guest name is required", booking: null };
      if (!isValidEmail(email))
        return { ok: false, message: "Invalid email", booking: null };

      const id = nanoid(10);
      const confirmationId = `RC-${nanoid(8).toUpperCase()}`;
      const createdAt = new Date().toISOString();
      const booking = { id, offerId, guestName, email, createdAt, confirmationId };

      bookings.push(booking);
      return { ok: true, message: "Booking confirmed", booking };
    },
  },
};

const yoga = createYoga({
  schema: createSchema({ typeDefs, resolvers }),
  graphqlEndpoint: "/graphql",
  cors: { origin: "*", credentials: false },
});

const server = createServer(yoga);
const PORT = process.env.PORT || 4000;

// IMPORTANT: bind to 0.0.0.0 so Android Emulator (10.0.2.2) can connect
server.listen(PORT, "0.0.0.0", () => {
  console.log(`GraphQL ready at http://localhost:${PORT}/graphql`);
});
