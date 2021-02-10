# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: audio_processor.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='audio_processor.proto',
  package='',
  syntax='proto3',
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x15\x61udio_processor.proto\"<\n\x0b\x41udioSample\x12\n\n\x02id\x18\x01 \x01(\x11\x12\x11\n\ttimestamp\x18\x02 \x01(\x03\x12\x0e\n\x06sample\x18\x03 \x01(\x0c\"9\n\x11SpectrumComponent\x12\x11\n\tfrequency\x18\x01 \x01(\x05\x12\x11\n\tamplitude\x18\x02 \x01(\x05\"Q\n\x08Spectrum\x12\n\n\x02id\x18\x01 \x01(\x11\x12\x11\n\ttimestamp\x18\x02 \x01(\x03\x12&\n\ncomponents\x18\x03 \x03(\x0b\x32\x12.SpectrumComponent2=\n\rAudioAnalyzer\x12,\n\x0f\x43omputeSpectrum\x12\x0c.AudioSample\x1a\t.Spectrum\"\x00\x62\x06proto3'
)




_AUDIOSAMPLE = _descriptor.Descriptor(
  name='AudioSample',
  full_name='AudioSample',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='AudioSample.id', index=0,
      number=1, type=17, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='AudioSample.timestamp', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='sample', full_name='AudioSample.sample', index=2,
      number=3, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=b"",
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=25,
  serialized_end=85,
)


_SPECTRUMCOMPONENT = _descriptor.Descriptor(
  name='SpectrumComponent',
  full_name='SpectrumComponent',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='frequency', full_name='SpectrumComponent.frequency', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='amplitude', full_name='SpectrumComponent.amplitude', index=1,
      number=2, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=87,
  serialized_end=144,
)


_SPECTRUM = _descriptor.Descriptor(
  name='Spectrum',
  full_name='Spectrum',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='Spectrum.id', index=0,
      number=1, type=17, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='Spectrum.timestamp', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='components', full_name='Spectrum.components', index=2,
      number=3, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=146,
  serialized_end=227,
)

_SPECTRUM.fields_by_name['components'].message_type = _SPECTRUMCOMPONENT
DESCRIPTOR.message_types_by_name['AudioSample'] = _AUDIOSAMPLE
DESCRIPTOR.message_types_by_name['SpectrumComponent'] = _SPECTRUMCOMPONENT
DESCRIPTOR.message_types_by_name['Spectrum'] = _SPECTRUM
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

AudioSample = _reflection.GeneratedProtocolMessageType('AudioSample', (_message.Message,), {
  'DESCRIPTOR' : _AUDIOSAMPLE,
  '__module__' : 'audio_processor_pb2'
  # @@protoc_insertion_point(class_scope:AudioSample)
  })
_sym_db.RegisterMessage(AudioSample)

SpectrumComponent = _reflection.GeneratedProtocolMessageType('SpectrumComponent', (_message.Message,), {
  'DESCRIPTOR' : _SPECTRUMCOMPONENT,
  '__module__' : 'audio_processor_pb2'
  # @@protoc_insertion_point(class_scope:SpectrumComponent)
  })
_sym_db.RegisterMessage(SpectrumComponent)

Spectrum = _reflection.GeneratedProtocolMessageType('Spectrum', (_message.Message,), {
  'DESCRIPTOR' : _SPECTRUM,
  '__module__' : 'audio_processor_pb2'
  # @@protoc_insertion_point(class_scope:Spectrum)
  })
_sym_db.RegisterMessage(Spectrum)



_AUDIOANALYZER = _descriptor.ServiceDescriptor(
  name='AudioAnalyzer',
  full_name='AudioAnalyzer',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_start=229,
  serialized_end=290,
  methods=[
  _descriptor.MethodDescriptor(
    name='ComputeSpectrum',
    full_name='AudioAnalyzer.ComputeSpectrum',
    index=0,
    containing_service=None,
    input_type=_AUDIOSAMPLE,
    output_type=_SPECTRUM,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
])
_sym_db.RegisterServiceDescriptor(_AUDIOANALYZER)

DESCRIPTOR.services_by_name['AudioAnalyzer'] = _AUDIOANALYZER

# @@protoc_insertion_point(module_scope)