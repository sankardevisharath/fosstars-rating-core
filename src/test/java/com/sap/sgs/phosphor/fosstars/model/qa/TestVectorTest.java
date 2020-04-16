package com.sap.sgs.phosphor.fosstars.model.qa;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.sgs.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExampleVectorsTest;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExampleVerification;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class TestVectorTest {

  private static final DoubleInterval NO_EXPECTED_SCORE = null;

  private static final boolean NOT_APPLICABLE = true;

  @Test
  public void smoke() {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    TestVector vector = new TestVector(values, expectedScore, SecurityLabelExample.OKAY, "test");

    assertFalse(vector.expectsNotApplicableScore());
    assertFalse(vector.values().isEmpty());
    assertTrue(vector.values().contains(
        new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1)));
    assertFalse(vector.values().contains(
        new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 2)));
    assertFalse(vector.values().contains(
        new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1)));
    assertEquals(DoubleInterval.init().from(4.0).to(6.4).closed().make(), vector.expectedScore());
    assertNotEquals(DoubleInterval.init().from(4.0).to(6.4).open().make(), vector.expectedScore());
    assertEquals(SecurityLabelExample.OKAY, vector.expectedLabel());
  }

  @Test(expected = NullPointerException.class)
  public void noValues() {
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    new TestVector(null, expectedScore, SecurityLabelExample.OKAY, "test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyValues() {
    Set<Value> values = new HashSet<>();
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    new TestVector(values, expectedScore, SecurityLabelExample.OKAY, "test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void noExpectedScore() {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    new TestVector(values, NO_EXPECTED_SCORE, SecurityLabelExample.OKAY, "test");
  }

  @Test
  public void notApplicableScore() {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    new TestVector(values, NO_EXPECTED_SCORE, SecurityLabelExample.OKAY, "test", NOT_APPLICABLE);
  }

  @Test
  public void noLabel() {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    TestVector vector = new TestVector(values, expectedScore, null, "test");
    assertNull(vector.expectedLabel());
  }

  @Test
  public void equalsAndHashcode() {
    final Interval expectedScoreOne = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    final Interval expectedScoreTwo = DoubleInterval.init().from(1.0).to(3.2).closed().make();

    assertEquals(
        newTestVector()
            .set(allUnknown(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(allUnknown(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());
    assertEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());

    assertNotEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());
    assertNotEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreTwo)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());
    assertNotEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.AWFUL)
            .alias("test")
            .make());
  }

  @Test
  public void storeAndLoadTestVectorsToJson() throws IOException {
    Path path = Files.createTempFile(SecurityRatingExampleVectorsTest.class.getName(), "test");
    try {
      byte[] content = Files.readAllBytes(path);
      assertNotNull(content);
      assertEquals(0, content.length);
      TestVector.storeTestVectorsToJson(
          path.toString(), SecurityRatingExampleVerification.TEST_VECTORS);
      content = Files.readAllBytes(path);
      assertNotNull(content);
      assertTrue(content.length > 0);
      List<TestVector> actualVectors = TestVector.loadTestVectorsFromJson(path.toString());
      assertNotNull(actualVectors);
      assertNotSame(SecurityRatingExampleVerification.TEST_VECTORS, actualVectors);
      assertEquals(SecurityRatingExampleVerification.TEST_VECTORS.size(), actualVectors.size());
      assertTrue(actualVectors.containsAll(SecurityRatingExampleVerification.TEST_VECTORS));
    } finally {
      Files.deleteIfExists(path);
    }
  }

  @Test
  public void yamlSerializeAndDeserialize() throws IOException {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    TestVector vector = new TestVector(values, expectedScore, SecurityLabelExample.OKAY, "test");

    YAMLFactory factory = new YAMLFactory();
    factory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
    ObjectMapper mapper = new ObjectMapper(factory);
    byte[] bytes = mapper.writeValueAsBytes(vector);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    TestVector clone = mapper.readValue(bytes, TestVector.class);
    assertEquals(vector, clone);
    assertEquals(vector.hashCode(), clone.hashCode());
  }

  @Test
  public void jsonSerializeAndDeserialize() throws IOException {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    TestVector vector = new TestVector(values, expectedScore, SecurityLabelExample.OKAY, "test");

    ObjectMapper mapper = new ObjectMapper();
    byte[] bytes = mapper.writeValueAsBytes(vector);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    TestVector clone = mapper.readValue(bytes, TestVector.class);
    assertEquals(vector, clone);
    assertEquals(vector.hashCode(), clone.hashCode());
  }

  @Test
  public void jsonSerializeAndDeserializeWithNotApplicableScoreValue() throws IOException {
    Set<Value> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    TestVector vector = new TestVector(values, null, SecurityLabelExample.OKAY, "test", true);

    ObjectMapper mapper = new ObjectMapper();
    byte[] bytes = mapper.writeValueAsBytes(vector);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    TestVector clone = mapper.readValue(bytes, TestVector.class);
    assertEquals(vector, clone);
    assertEquals(vector.hashCode(), clone.hashCode());
  }

}
